package dev.upaya.kasina.flashlight


enum class FlashlightState {
    OFF,
    OFF_IN_SESSION,
    TRANSITION_TO_ON,
    TRANSITION_TO_OFF,
    ON_SWITCHED,
    ON_HOLDING,
}
