package dev.upaya.kasina.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.upaya.kasina.R
import dev.upaya.kasina.data.SessionState
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun FlameIcon(
    modifier: Modifier = Modifier,
    sessionState: SessionState,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
    ) {

        val flameOuterColor = sessionState.getColor()
        val flameInnerColor = sessionState.getNextColor()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawCircle(
                color = flameInnerColor,
                center = Offset(
                    size.width  / 2f,
                    size.height / 2f + (.245f * size.height)),
                radius = size.minDimension / 4f,
            )
        }

        // Simulate a key press on value change
        val interactionSource = remember { MutableInteractionSource() }.also {
            LaunchedEffect(sessionState) { it.simulatePress() }
        }

        Icon(
            painter = painterResource(R.drawable.fire_svgrepo_com),
            modifier = Modifier
                .graphicsLayer {
                    scaleX = 1.20f
                    scaleY = 1.23f
                    translationX =  0.025f * size.width
                    translationY = -0.015f * size.height
                }
                .clickable(
                    onClick = {},
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = false,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ),
            contentDescription = "Fire",
            tint = flameOuterColor,
        )
    }
}


@Preview
@Composable
private fun FlameIconPreview() {
    FlashKasinaTheme(darkTheme = true, dynamicColor = false) {
        FlameIcon(
            sessionState = SessionState.INACTIVE,
        )
    }
}
