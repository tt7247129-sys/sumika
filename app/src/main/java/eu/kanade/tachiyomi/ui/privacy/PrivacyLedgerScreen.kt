package eu.kanade.tachiyomi.ui.privacy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.kanade.presentation.components.AppBar
import eu.kanade.tachiyomi.network.PrivacyLedger
import eu.kanade.tachiyomi.network.PrivacyLedgerEntry
import mihon.core.navigation.LocalNavigator
import tachiyomi.core.common.i18n.stringResource as contextStringResource
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.i18n.stringResource

/**
 * Read-only audit surface for everything the app talks to.
 *
 * This screen is the honest counterweight to every competitor's "we respect your privacy" blurb:
 * it cannot be edited by marketing, because it is generated from the live HTTP stack.
 */
@Composable
fun PrivacyLedgerScreen() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current
    val entries by PrivacyLedger.entries.collectAsState()

    Scaffold(
        topBar = { scrollBehavior ->
            AppBar(
                title = stringResource(MR.strings.pref_privacy_ledger),
                navigateUp = navigator::pop,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item(key = "header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(MR.strings.privacy_ledger_intro),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = stringResource(MR.strings.privacy_ledger_stat_hosts, entries.size),
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Text(
                            text = stringResource(MR.strings.privacy_ledger_stat_requests, PrivacyLedger.requestCount()),
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Text(
                            text = stringResource(MR.strings.privacy_ledger_stat_trackers, PrivacyLedger.trackerHits()),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (PrivacyLedger.trackerHits() > 0) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedButton(
                            onClick = { copyReport(context) },
                        ) {
                            Text(text = stringResource(MR.strings.privacy_ledger_action_copy))
                        }
                        OutlinedButton(
                            onClick = { PrivacyLedger.clear() },
                        ) {
                            Text(text = stringResource(MR.strings.privacy_ledger_action_clear))
                        }
                    }
                    if (entries.isEmpty()) {
                        Text(
                            text = stringResource(MR.strings.privacy_ledger_empty),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            items(
                items = entries,
                key = { it.host },
            ) { entry ->
                LedgerRow(entry)
            }

            item(key = "footnote") {
                Text(
                    text = stringResource(MR.strings.privacy_ledger_footnote),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun LedgerRow(entry: PrivacyLedgerEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.host,
                style = MaterialTheme.typography.bodyMedium,
                color = if (entry.isKnownTracker) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
            if (entry.isKnownTracker) {
                Text(
                    text = stringResource(MR.strings.privacy_ledger_tracker_row),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Text(
            text = entry.requests.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun copyReport(context: Context) {
    val report = PrivacyLedger.exportReport(buildLabel(context))
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    clipboard?.setPrimaryClip(ClipData.newPlainText("Sumika Privacy Ledger", report))
    Toast.makeText(
        context,
        context.contextStringResource(MR.strings.privacy_ledger_copied),
        Toast.LENGTH_SHORT,
    ).show()
}

@Suppress("DEPRECATION")
private fun buildLabel(context: Context): String = runCatching {
    val info = context.packageManager.getPackageInfo(context.packageName, 0)
    "${info.packageName} ${info.versionName}"
}.getOrDefault(context.packageName)
