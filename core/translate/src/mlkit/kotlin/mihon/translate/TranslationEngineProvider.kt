package mihon.translate

/**
 * ML Kit build: the on-device engine is available in every distribution except
 * FOSS. First use of a language pair downloads the model from Google's CDN;
 * recognition and translation afterwards are fully offline.
 */
object TranslationEngineProvider {
    fun isAvailable(): Boolean = true

    fun create(): TranslationEngine = MlKitTranslationEngine()
}
