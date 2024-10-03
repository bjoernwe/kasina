package dev.upaya.kasina.data

import dev.upaya.kasina.flashlight.FlashlightEvent
import org.junit.Assert
import org.junit.Test
import java.time.Instant


class FlashlightEventsRepositoryTest {

    @Test
    fun calcSessions() {

        // GIVEN
        val events = listOf(
            FlashlightEvent(false, Instant.ofEpochSecond(1)),  // skip
            FlashlightEvent(false, Instant.ofEpochSecond(2)),  // skip
            FlashlightEvent(false, Instant.ofEpochSecond(3)),  // skip
            FlashlightEvent(true,  Instant.ofEpochSecond(4)),  // *** ON  ***
            FlashlightEvent(true,  Instant.ofEpochSecond(5)),  // redundant
            FlashlightEvent(false, Instant.ofEpochSecond(6)),  // *** OFF ***
            FlashlightEvent(false, Instant.ofEpochSecond(7)),  // redundant
            FlashlightEvent(false, Instant.ofEpochSecond(8)),  // redundant
            FlashlightEvent(true,  Instant.ofEpochSecond(9)),  // *** ON  ***
            FlashlightEvent(true,  Instant.ofEpochSecond(10)), // redundant (but out of scope)
        )

        // WHEN
        val result = FlashlightEventsRepository.calcSessions(events)[0]

        // THEN
        val expectedSession = Session(2L, 3L)
        Assert.assertEquals(expectedSession, result)
    }
}
