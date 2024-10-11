package dev.upaya.kasina.inputkeys

import dev.upaya.kasina.flashlight.Flashlight
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InputKeyHandler @Inject constructor(
    private val flashlight: Flashlight,
) {

    private val button = PressableButton()
    val buttonState = button.buttonState

    fun handleButtonPress(scope: CoroutineScope) {
        if (!flashlight.flashlightAvailable)
            return
        button.press(scope = scope)
    }

    fun handleButtonRelease() {
        if (!flashlight.flashlightAvailable)
            return
        button.release()
    }
}
