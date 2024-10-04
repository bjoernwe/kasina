package dev.upaya.kasina.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.data.Session


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

        val maxOnDuration = filteredSessions.maxOfOrNull { it?.onDuration ?: 1 } ?: 1
        val maxOffDuration = filteredSessions.maxOfOrNull { it?.offDuration ?: 1 } ?: 1

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

                val onDuration = session.onDuration.coerceAtLeast(1)
                val offDuration = session.offDuration.coerceAtLeast(1)
                val onDurationGap = maxOnDuration - onDuration
                val offDurationGap = maxOffDuration - offDuration

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {

                    // ON duration filler
                    if (offDurationGap > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(offDurationGap.toFloat())
                        ) { }
                    }

                    // ON duration
                    Surface(
                        shape = RoundedCornerShape(50, 50),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier
                            .weight(offDuration.toFloat().coerceAtLeast(.01f))
                            .fillMaxWidth()
                    ) { }

                    // OFF duration
                    Surface(
                        shape = RoundedCornerShape(0, 0, 50, 50),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .weight(onDuration.toFloat().coerceAtLeast(.01f))
                            .fillMaxWidth()
                    ) { }

                    // OFF duration filler
                    if (onDurationGap > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(onDurationGap.toFloat())
                        ) { }
                    }
                }
            }
    }
}