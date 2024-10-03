package dev.upaya.kasina.flashlight

import java.time.Instant


data class FlashlightEvent(
    val isOn: Boolean,
    val timestamp: Instant = Instant.now(),
) {
    val isOff: Boolean
        get() = !isOn
}
