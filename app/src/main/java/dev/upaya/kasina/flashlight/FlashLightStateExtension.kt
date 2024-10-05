package dev.upaya.kasina.flashlight

import dev.upaya.kasina.inputkeys.PressableButtonState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update


internal suspend fun MutableStateFlow<FlashlightState>.updateFlashlightStateOnInputEvents(volumeKeysState: Flow<PressableButtonState>, isFlashlightOn: StateFlow<Boolean>, calcNewState: (FlashlightState, PressableButtonState, Boolean) -> FlashlightState) {
    volumeKeysState
        .combine(isFlashlightOn) { keyEvent, flashlightEvent -> keyEvent to flashlightEvent }
        .collect { (keyEvent, isOn) ->
            this.update { currentState ->
                calcNewState(currentState, keyEvent, isOn)
            }
        }
}
