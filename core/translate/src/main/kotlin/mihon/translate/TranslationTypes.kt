package mihon.translate

import android.graphics.RectF

/** Languages supported by the on-device translation engine. */
enum class TranslateLang(val code: String, val displayName: String) {
    AUTO("auto", "Auto detect"),
    JAPANESE("ja", "日本語"),
    ENGLISH("en", "English"),
    CHINESE("zh", "中文"),
    KOREAN("ko", "한국어"),
    SPANISH("es", "Español"),
    FRENCH("fr", "Français"),
    GERMAN("de", "Deutsch"),
    ITALIAN("it", "Italiano"),
    PORTUGUESE("pt", "Português"),
    RUSSIAN("ru", "Русский"),
    INDONESIAN("id", "Indonesia"),
    THAI("th", "ไทย"),
    VIETNAMESE("vi", "Tiếng Việt"),
    ARABIC("ar", "العربية"),
    HINDI("hi", "हिन्दी"),
}

/** One translated region of the page, with its bounding box in bitmap coordinates. */
data class TranslatedBlock(
    val rect: RectF,
    val original: String,
    val translated: String,
)

data class TranslationResult(val blocks: List<TranslatedBlock>)

/**
 * Contract for the on-page translation pipeline:
 * bitmap in → OCR per text region → on-device translation per region.
 *
 * Implementations must not send the bitmap or text off the device beyond what
 * is documented in the privacy policy (first-use model download only).
 */
interface TranslationEngine {
    /** False when the build intentionally excludes the engine (FOSS distribution). */
    val isAvailable: Boolean

    suspend fun translate(
        bitmap: android.graphics.Bitmap,
        sourceLang: TranslateLang,
        targetLang: TranslateLang,
    ): TranslationResult
}
