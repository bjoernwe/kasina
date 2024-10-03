package dev.upaya.kasina.flashlight

import java.time.Instant


class FlashlightEvent {

    @Suppress("unused")  // Firebase Realtime Database needs a default constructor
    constructor()

    constructor(isOn: Boolean, timestamp: Instant = Instant.now()) {
        this.isOn = isOn
        this.timestamp = timestamp.epochSecond
    }

    var timestamp: Long = 0L
    var isOn: Boolean = false
}
