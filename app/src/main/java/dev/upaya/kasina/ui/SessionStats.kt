package dev.upaya.kasina.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.data.Session
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@Composable
fun SessionStats(
    sessions: List<Session?>,
    modifier: Modifier = Modifier,
    numBars: Int = 14,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        val filteredSessions = sessions.take(numBars)

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

                val onDuration = session.onDuration
                val offDuration = session.offDuration
                val totalDuration = onDuration + offDuration
                val onDurationGap = filteredSessions.maxOnDuration() - onDuration
                val offDurationGap = filteredSessions.maxOffDuration() - offDuration

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

                    // OFF/ON bar
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier
                            .weight(totalDuration)
                            .fillMaxWidth()
                    ) {
                        Box (
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0.0f to MaterialTheme.colorScheme.primaryContainer,
                                            session.gradientTransitionPoint().minus(.0f) to MaterialTheme.colorScheme.tertiaryContainer,
                                            session.gradientTransitionPoint().plus( .0f) to MaterialTheme.colorScheme.onTertiaryContainer,
                                            1.0f to MaterialTheme.colorScheme.onTertiaryContainer,
                                        ),
                                    )
                                )
                        ) { }
                    }

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