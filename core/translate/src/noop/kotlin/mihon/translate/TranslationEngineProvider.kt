package mihon.translate

/**
 * FOSS build: proprietary ML Kit binaries are excluded, so translation is
 * unavailable.
 */
object TranslationEngineProvider {
    fun isAvailable(): Boolean = false

    fun create(): TranslationEngine = throw UnsupportedOperationException(
        "Translation engine not included in this build",
    )
}
