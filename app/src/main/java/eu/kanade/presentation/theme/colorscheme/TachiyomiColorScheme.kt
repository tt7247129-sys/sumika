package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Colors for Default theme — Sumika
 *
 * Key colors (brand):
 * Ink     #0E1A1F  (dark background)
 * Paper   #F4EDE1  (light background, page tint)
 * Gold    #C9A227  (primary accent)
 *
 * Tonal values follow the Material 3 tone scale (40 for light, 80 for dark) so the
 * palette stays accessible (WCAG AA on body text) without hand-tuning every pair.
 */
internal object TachiyomiColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        primary = Color(0xFFE9CE8C),
        onPrimary = Color(0xFF3C2F00),
        primaryContainer = Color(0xFF584500),
        onPrimaryContainer = Color(0xFFFFE39C),
        inversePrimary = Color(0xFF7A5F00),
        secondary = Color(0xFFAFC8D4), // Unread badge
        onSecondary = Color(0xFF16323C), // Unread badge text
        secondaryContainer = Color(0xFF2E4954), // Navigation bar selector pill & progress indicator (remaining)
        onSecondaryContainer = Color(0xFFCDE7F5), // Navigation bar selector icon
        tertiary = Color(0xFF8FD6C7), // Downloaded badge
        onTertiary = Color(0xFF003731), // Downloaded badge text
        tertiaryContainer = Color(0xFF005049),
        onTertiaryContainer = Color(0xFFACEEDE),
        background = Color(0xFF0E1A1F),
        onBackground = Color(0xFFE6E4DD),
        surface = Color(0xFF0E1A1F),
        onSurface = Color(0xFFE6E4DD),
        surfaceVariant = Color(0xFF121F24), // Navigation bar background (ThemePrefWidget)
        onSurfaceVariant = Color(0xFFC3C7C2),
        surfaceTint = Color(0xFFE9CE8C),
        inverseSurface = Color(0xFFE6E4DD),
        inverseOnSurface = Color(0xFF0E1A1F),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        outline = Color(0xFF8D9191),
        outlineVariant = Color(0xFF2C383C),
        surfaceContainerLowest = Color(0xFF0A1418),
        surfaceContainerLow = Color(0xFF121F24),
        surfaceContainer = Color(0xFF16242A), // Navigation bar background
        surfaceContainerHigh = Color(0xFF1E2C32),
        surfaceContainerHighest = Color(0xFF28373D),
    )

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF7A5F00),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFE39C),
        onPrimaryContainer = Color(0xFF251A00),
        inversePrimary = Color(0xFFE9CE8C),
        secondary = Color(0xFF2E4954), // Unread badge
        onSecondary = Color(0xFFFFFFFF), // Unread badge text
        secondaryContainer = Color(0xFFCDE7F5), // Navigation bar selector pill & progress indicator (remaining)
        onSecondaryContainer = Color(0xFF051F28), // Navigation bar selector icon
        tertiary = Color(0xFF00695F), // Downloaded badge
        onTertiary = Color(0xFFFFFFFF), // Downloaded badge text
        tertiaryContainer = Color(0xFFA6F2E4),
        onTertiaryContainer = Color(0xFF00201C),
        background = Color(0xFFFBF8F1),
        onBackground = Color(0xFF1B1C18),
        surface = Color(0xFFFBF8F1),
        onSurface = Color(0xFF1B1C18),
        surfaceVariant = Color(0xFFF0EDE4), // Navigation bar background (ThemePrefWidget)
        onSurfaceVariant = Color(0xFF44474A),
        surfaceTint = Color(0xFF7A5F00),
        inverseSurface = Color(0xFF30312C),
        inverseOnSurface = Color(0xFFF3F1E7),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        outline = Color(0xFF75777A),
        outlineVariant = Color(0xFFC5C6C4),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF6F3EB),
        surfaceContainer = Color(0xFFF0EDE4), // Navigation bar background
        surfaceContainerHigh = Color(0xFFEAE7DE),
        surfaceContainerHighest = Color(0xFFE4E1D8),
    )
}
