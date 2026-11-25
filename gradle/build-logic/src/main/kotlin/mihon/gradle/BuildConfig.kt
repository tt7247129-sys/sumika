package mihon.gradle

import org.gradle.api.Project

interface BuildConfig {
    val includeTelemetry: Boolean
    val uploadCrashlyticsMapping: Boolean
    val enableUpdater: Boolean
    val includeDependencyInfo: Boolean
    val includeTranslate: Boolean
}

private enum class Distribution {
    LOCAL,
    CI,
    GITHUB,
    FOSS,
}

val Project.Config: BuildConfig get() = object : BuildConfig {
    private val distribution: Distribution = project.providers.gradleProperty("dist").orNull
        ?.let { name ->
            Distribution.entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: error("Unknown -Pdist=$name, expected one of $distributionNames")
        }
        ?: Distribution.LOCAL

    /**
     * Sumika: hard-disabled, deliberately not overridable.
     *
     * "No tracking" is a product guarantee, not a build flag. If this could be
     * flipped by `-Pinclude-telemetry=true`, our Privacy Ledger would be able to
     * contradict our own store listing. Upstream Mihon defaults this to true for
     * CI/GitHub distributions; we never ship that path.
     */
    override val includeTelemetry: Boolean = false

    override val uploadCrashlyticsMapping: Boolean = includeTelemetry && (distribution == Distribution.GITHUB)

    override val enableUpdater: Boolean = project.flag("enable-updater")
        ?: (distribution != Distribution.LOCAL)

    override val includeDependencyInfo: Boolean = project.flag("include-dependency-info") ?: false

    /**
     * Sumika: on-device translation ships in every distribution except the FOSS
     * one, where proprietary ML Kit binaries would violate F-Droid policy.
     * The engine itself runs fully offline; only first-use model downloads
     * reach Google's CDN, and that is disclosed in the privacy policy.
     */
    override val includeTranslate: Boolean = distribution != Distribution.FOSS
}

private val distributionNames = Distribution.entries.joinToString { it.name.lowercase() }

private fun Project.flag(name: String): Boolean? = providers.gradleProperty(name).orNull
    ?.let { it.isEmpty() || it.toBoolean() }
