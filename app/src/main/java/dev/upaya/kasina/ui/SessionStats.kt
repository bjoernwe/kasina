package dev.upaya.kasina.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun SessionStats(
    sessions: List<Session?>,
    modifier: Modifier = Modifier,
    numBars: Int = 10,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        val filteredSessions = sessions.take(numBars)

        val maxOnDuration  = filteredSessions.maxOfOrNull { it?.onDuration  ?: 1f } ?: 1f
        val maxOffDuration = filteredSessions.maxOfOrNull { it?.offDuration ?: 1f } ?: 1f

        filteredSessions
            .asReversed()
            .toMutableList()
            .also {
                val numMissingValues = (numBars - it.size).coerceAtLeast(0)
                val missingValues = List(numMissingValues) { null }
                it.addAll( missingValues )
            }.forEach { session ->

                if (session == null) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                    ) { }
                    return@forEach
                }

                val onDuration = session.onDuration.coerceAtLeast(1f)
                val offDuration = session.offDuration.coerceAtLeast(1f)
                val onDurationGap = maxOnDuration - onDuration
                val offDurationGap = maxOffDuration - offDuration

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {

                    // OFF duration filler
                    if (offDurationGap > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(offDurationGap)
                        ) { }
                    }

                    // OFF duration
                    Surface(
                        shape = RoundedCornerShape(50, 50),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier
                            .weight(offDuration.coerceAtLeast(1f))
                            .fillMaxWidth()
                    ) { }

                    Box (
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.tertiaryContainer,
                                        MaterialTheme.colorScheme.onTertiaryContainer
                                    ),
                                )
                            )
                    ) { }

                    // ON duration
                    Surface(
                        shape = RoundedCornerShape(0, 0, 50, 50),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .weight(onDuration.coerceAtLeast(1f))
                            .fillMaxWidth()
                    ) { }

                    // ON duration filler
                    if (onDurationGap > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(onDurationGap)
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