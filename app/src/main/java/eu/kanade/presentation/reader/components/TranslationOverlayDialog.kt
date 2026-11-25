package eu.kanade.presentation.reader.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import eu.kanade.tachiyomi.ui.reader.model.ReaderPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mihon.icons.materialsymbols.MaterialSymbols
import mihon.icons.materialsymbols.rounded.Close
import mihon.translate.TranslateLang
import mihon.translate.TranslationEngine
import mihon.translate.TranslationResult
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource
import kotlin.math.roundToInt

/**
 * Sumika: full-screen dialog that runs the on-device translation pipeline on the
 * current page and paints the translated text back over the page regions.
 */
@Composable
fun TranslationOverlayDialog(
    page: ReaderPage,
    engine: TranslationEngine,
    initialSource: TranslateLang,
    initialTarget: TranslateLang,
    onDismiss: () -> Unit,
    onSourceChange: (TranslateLang) -> Unit,
    onTargetChange: (TranslateLang) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }
        var result by remember { mutableStateOf<TranslationResult?>(null) }
        var running by remember { mutableStateOf(false) }
        var failed by remember { mutableStateOf<String?>(null) }
        var source by remember { mutableStateOf(initialSource) }
        var target by remember { mutableStateOf(initialTarget) }

        // Decode the page bitmap once. ReaderPage.stream is the file-backed
        // stream the loaders install; downloadStream is the fallback.
        LaunchedEffect(page) {
            bitmap = withContext(Dispatchers.IO) {
                val stream = page.stream?.invoke() ?: page.downloadStream?.reader()
                try {
                    stream?.use { BitmapFactory.decodeStream(it) }
                } catch (e: Exception) {
                    null
                }
            }
        }

        suspend fun runTranslation(bmp: Bitmap) {
            running = true
            failed = null
            result = null
            try {
                result = engine.translate(bmp, source, target)
            } catch (e: Exception) {
                failed = e.message ?: e.javaClass.simpleName
            } finally {
                running = false
            }
        }

        // Translate automatically once the bitmap is ready; rerun when the
        // language pair changes.
        LaunchedEffect(bitmap, source, target) {
            val bmp = bitmap ?: return@LaunchedEffect
            if (!running) runTranslation(bmp)
        }

        val density = LocalDensity.current

        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.92f))) {
            val bmp = bitmap

            if (bmp == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                BoxWithConstraints(
                    modifier = Modifier.align(Alignment.Center).fillMaxSize(),
                ) {
                    val boxW = constraints.maxWidth.toFloat()
                    val boxH = constraints.maxHeight.toFloat()
                    val bmpW = bmp.width.toFloat()
                    val bmpH = bmp.height.toFloat()
                    val scale = minOf(boxW / bmpW, boxH / bmpH)
                    val dispW = (bmpW * scale).roundToInt()
                    val dispH = (bmpH * scale).roundToInt()

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .requiredWidth(with(density) { dispW.toFloat().toDp() })
                            .requiredHeight(with(density) { dispH.toFloat().toDp() }),
                    ) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                        )

                        // Translated text painted back over each OCR region.
                        result?.blocks?.forEach { block ->
                            val left = (block.rect.left * scale).roundToInt()
                            val top = (block.rect.top * scale).roundToInt()
                            val w = block.rect.width() * scale
                            val h = block.rect.height() * scale
                            Text(
                                text = block.translated,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = (h * 0.42f).sp,
                                    lineHeight = (h * 0.5f).sp,
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .offset { IntOffset(left, top) }
                                    .requiredWidth(with(density) { w.toDp() })
                                    .requiredHeight(with(density) { h.toDp() })
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f),
                                        RoundedCornerShape(3.dp),
                                    )
                                    .padding(2.dp),
                            )
                        }
                    }
                }
            }

            // Top bar: language selectors + close.
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LangSelector(
                    label = stringResource(MR.strings.reader_translation_source),
                    selected = source,
                    enabled = !running,
                    includeAuto = true,
                    onSelect = {
                        source = it
                        onSourceChange(it)
                    },
                )
                IconButton(onClick = onDismiss) {
                    Icon(MaterialSymbols.Rounded.Close, contentDescription = null)
                }
            }

            // Bottom status.
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when {
                    running -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(MR.strings.reader_translation_running))
                    }
                    failed != null -> Text(
                        text = stringResource(MR.strings.reader_translation_failed, failed ?: ""),
                        color = MaterialTheme.colorScheme.error,
                    )
                    result != null && result?.blocks?.isEmpty() == true -> {
                        Text(stringResource(MR.strings.reader_translation_no_text))
                    }
                }
                Text(
                    text = stringResource(MR.strings.reader_translation_offline_note),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun LangSelector(
    label: String,
    selected: TranslateLang,
    enabled: Boolean,
    includeAuto: Boolean,
    onSelect: (TranslateLang) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        TextButton(onClick = { if (enabled) expanded = true }, enabled = enabled) {
            Text("$label: ${selected.displayName}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (includeAuto) {
                DropdownMenuItem(
                    text = { Text(TranslateLang.AUTO.displayName) },
                    onClick = {
                        expanded = false
                        onSelect(TranslateLang.AUTO)
                    },
                )
            }
            TranslateLang.entries
                .filter { it != TranslateLang.AUTO }
                .forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(lang.displayName) },
                        onClick = {
                            expanded = false
                            onSelect(lang)
                        },
                    )
                }
        }
    }
}
