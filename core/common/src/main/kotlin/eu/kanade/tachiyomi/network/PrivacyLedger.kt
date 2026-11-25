package eu.kanade.tachiyomi.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * One aggregated row of the privacy ledger: a host we talked to, how often, and when last.
 */
data class PrivacyLedgerEntry(
    val host: String,
    val requests: Long,
    val lastSeen: Long,
    val isKnownTracker: Boolean,
)

/**
 * Sumika's Privacy Ledger.
 *
 * Commercial readers *promise* they do not track you. This records what the app actually does:
 * every host the OkHttp stack contacts is counted here and shown to the user, row by row.
 *
 * Deliberate design constraints:
 *  - In-memory and on-device only. The ledger itself is never uploaded, and never written to disk
 *    unless the user explicitly copies the report.
 *  - No per-URL, no query strings, no cookies, no headers. Only the host name is kept, because the
 *    question the user is asking is "who do you talk to", not "what did I read".
 *  - Zero cost when nobody looks at it: recording is a hash-map increment.
 */
object PrivacyLedger {

    /**
     * Hosts that exist only to profile people. If any of these ever shows up in the ledger,
     * our "no tracking" claim is false and the user deserves to see it in red.
     */
    private val KNOWN_TRACKERS = setOf(
        "google-analytics.com",
        "analytics.google.com",
        "app-measurement.com",
        "firebase-settings.crashlytics.com",
        "crashlytics.com",
        "doubleclick.net",
        "googlesyndication.com",
        "googleadservices.com",
        "adservice.google.com",
        "graph.facebook.com",
        "appsflyer.com",
        "adjust.com",
        "branch.io",
        "amplitude.com",
        "mixpanel.com",
        "segment.io",
        "sentry.io",
        "bugsnag.com",
        "onesignal.com",
        "unityads.unity3d.com",
        "applovin.com",
        "ironsrc.com",
        "vungle.com",
        "chartboost.com",
        "inmobi.com",
        "mopub.com",
        "pubmatic.com",
        "rubiconproject.com",
        "criteo.com",
        "taboola.com",
        "outbrain.com",
    )

    private val counters = ConcurrentHashMap<String, AtomicLong>()
    private val lastSeen = ConcurrentHashMap<String, Long>()
    private val _entries = MutableStateFlow<List<PrivacyLedgerEntry>>(emptyList())

    /** Live, sorted (most contacted first) view for the UI. */
    val entries: StateFlow<List<PrivacyLedgerEntry>> = _entries.asStateFlow()

    /** Epoch millis when this process started recording. The UI labels the window honestly. */
    val sessionStart: Long = System.currentTimeMillis()

    @Volatile
    private var totalRequests: Long = 0

    fun record(host: String) {
        if (host.isBlank()) return
        counters.getOrPut(host) { AtomicLong(0) }.incrementAndGet()
        lastSeen[host] = System.currentTimeMillis()
        totalRequests++
        publish()
    }

    fun clear() {
        counters.clear()
        lastSeen.clear()
        totalRequests = 0
        _entries.value = emptyList()
    }

    fun trackerHits(): Int = counters.keys.count { isTracker(it) }

    fun requestCount(): Long = totalRequests

    /** Tab-separated, paste-anywhere report. This is the artefact a skeptic can post in an issue. */
    fun exportReport(buildInfo: String): String = buildString {
        appendLine("# Sumika Privacy Ledger")
        appendLine("# build: $buildInfo")
        appendLine("# window: session (since ${java.util.Date(sessionStart)})")
        appendLine("# total requests: $totalRequests")
        appendLine("# distinct hosts: ${counters.size}")
        appendLine("# known-tracker hosts: ${trackerHits()}")
        appendLine("#")
        appendLine("# requests\thost\tlast_seen\tknown_tracker")
        sortedEntries().forEach { entry ->
            append(entry.requests).append('\t')
            append(entry.host).append('\t')
            append(java.util.Date(entry.lastSeen)).append('\t')
            append(if (entry.isKnownTracker) "YES" else "no").append('\n')
        }
    }

    private fun publish() {
        _entries.value = sortedEntries()
    }

    private fun sortedEntries(): List<PrivacyLedgerEntry> = counters.entries
        .map { (host, count) ->
            PrivacyLedgerEntry(
                host = host,
                requests = count.get(),
                lastSeen = lastSeen[host] ?: 0L,
                isKnownTracker = isTracker(host),
            )
        }
        .sortedWith(compareByDescending<PrivacyLedgerEntry> { it.isKnownTracker }.thenByDescending { it.requests })

    private fun isTracker(host: String): Boolean {
        val normalized = host.lowercase().removePrefix("www.")
        return KNOWN_TRACKERS.any { normalized == it || normalized.endsWith(".$it") }
    }
}

/**
 * Records the host of every outgoing request. Kept intentionally minimal: no reading of bodies,
 * no header capture, no timing detail.
 */
class PrivacyLedgerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        PrivacyLedger.record(chain.request().url.host)
        return chain.proceed(chain.request())
    }
}
