package dev.upaya.kasina.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun SessionStats(
    sessions: List<Session?>,
    modifier: Modifier = Modifier,
    numBars: Int = 15,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        val filteredSessions = sessions
            .take(numBars)
            .asReversed()
            .toMutableList()
            .also {
                val numMissingValues = (numBars - it.size).coerceAtLeast(0)
                val missingValues = List(numMissingValues) { null }
                it.addAll( missingValues )
            }

        filteredSessions.forEachIndexed { i, session ->

                if (session == null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                    ) { }
                    return@forEachIndexed
                }

                val onGapWeight = filteredSessions.onGapWeight(i)
                val offGapWeight = filteredSessions.offGapWeight(i)
                val totalOnOffWeight = filteredSessions.totalOnOffWeight(i)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {

                    // OFF duration filler
                    if (offGapWeight > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(offGapWeight)
                        ) { }
                    }

                    // OFF/ON bar
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier
                            .weight(totalOnOffWeight)
                            .fillMaxWidth()
                    ) {
                        val gradientPoint0 = 0f
                        val gradientPointX = session.gradientTransitionPoint()
                        val gradientPoint1 = gradientPointX.times(.7f)
                        val gradientPointY = session.gradientTransitionPoint().plus( filteredSessions.localFraction(i, .05f))
                        val gradientPointZ = 1f
                        Box (
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            gradientPoint0 to MaterialTheme.colorScheme.primaryContainer,
                                            gradientPoint1 to MaterialTheme.colorScheme.tertiaryContainer,
                                            gradientPointX to lerp(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, .25f),
                                            gradientPointY to MaterialTheme.colorScheme.onTertiaryContainer,
                                            gradientPointZ to MaterialTheme.colorScheme.onTertiaryContainer,
                                        ),
                                    )
                                )
                        ) { }
                    }

                    // ON duration filler
                    if (onGapWeight > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(onGapWeight)
                        ) { }
                    }
                }
            }
    }
}


@Preview
@Composable
private fun SessionStatsPreview() {
    FlashKasinaTheme(darkTheme = true, dynamicColor = false) {
        SessionStats(
            sessions = listOf(
                Session(
                    timestampFlash = 0,
                    timestampStart = 1,
                    timestampEnd = 5,
                ),
                Session(
                    timestampFlash = 0,
                    timestampStart = 2,
                    timestampEnd = 4,
                ),
            ),
        )
    }
}