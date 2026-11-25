package mihon.translate

import android.graphics.Bitmap

class NoopTranslationEngine : TranslationEngine {
    override val isAvailable: Boolean = false

    override suspend fun translate(
        bitmap: Bitmap,
        sourceLang: TranslateLang,
        targetLang: TranslateLang,
    ): TranslationResult = throw UnsupportedOperationException("Translation engine not included in this build")
}
