package dev.upaya.kasina.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
internal fun InfoErrorSurface(
    isFlashlightAvailable: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(15),
        color = if (isFlashlightAvailable) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.errorContainer,
        modifier = modifier,
    ) {
        if (isFlashlightAvailable)
            InfoText()
        else
            ErrorText()
    }
}
