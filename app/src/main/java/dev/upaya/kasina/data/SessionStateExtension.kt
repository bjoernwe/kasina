package dev.upaya.kasina.data

import dev.upaya.kasina.flashlight.FlashlightState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


fun StateFlow<FlashlightState>.toSessionState(): Flow<SessionState> {
    return this.map {
        when (it) {
            FlashlightState.OFF -> SessionState.INACTIVE
            FlashlightState.OFF_IN_SESSION -> SessionState.ACTIVE_OFF
            FlashlightState.TRANSITION_TO_ON -> SessionState.ACTIVE_ON
            FlashlightState.TRANSITION_TO_OFF -> SessionState.ACTIVE_ON
            FlashlightState.ON_SWITCHED -> SessionState.ACTIVE_ON
            FlashlightState.ON_HOLDING -> SessionState.ACTIVE_ON
        }
    }.distinctUntilChanged()
}
