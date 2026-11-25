package mihon.translate

import android.graphics.Bitmap
import android.graphics.RectF
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitTranslationEngine : TranslationEngine {

    override val isAvailable: Boolean = true

    private fun recognizerFor(lang: TranslateLang) = TextRecognition.getClient(
        when (lang) {
            TranslateLang.JAPANESE -> JapaneseTextRecognizerOptions.Builder().build()
            TranslateLang.CHINESE -> ChineseTextRecognizerOptions.Builder().build()
            TranslateLang.KOREAN -> KoreanTextRecognizerOptions.Builder().build()
            else -> TextRecognizerOptions.DEFAULT_OPTIONS
        },
    )

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun translate(
        bitmap: Bitmap,
        sourceLang: TranslateLang,
        targetLang: TranslateLang,
    ): TranslationResult {
        val source = if (sourceLang == TranslateLang.AUTO) TranslateLang.JAPANESE else sourceLang
        val target = if (targetLang == TranslateLang.AUTO) TranslateLang.ENGLISH else targetLang

        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = recognizerFor(source)
        val visionText = try {
            recognizer.process(image).await()
        } finally {
            recognizer.close()
        }

        val blocks = visionText.textBlocks
            .filter { it.boundingBox != null && it.text.isNotBlank() }
            .sortedWith(compareBy({ it.boundingBox!!.top }, { it.boundingBox!!.left }))
        if (blocks.isEmpty()) return TranslationResult(emptyList())

        val sourceMl = TranslateLanguage.fromLanguageTag(source.code)
            ?: throw IllegalArgumentException("Unsupported source language: ${source.code}")
        val targetMl = TranslateLanguage.fromLanguageTag(target.code)
            ?: throw IllegalArgumentException("Unsupported target language: ${target.code}")

        val translator = Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(sourceMl)
                .setTargetLanguage(targetMl)
                .build(),
        )
        try {
            translator.downloadModelIfNeeded().await()
            return TranslationResult(
                blocks.map { block ->
                    val translated = try {
                        translator.translate(block.text).await()
                    } catch (t: Throwable) {
                        block.text
                    }
                    TranslatedBlock(
                        rect = RectF(block.boundingBox!!),
                        original = block.text,
                        translated = translated,
                    )
                },
            )
        } finally {
            translator.close()
        }
    }
}
