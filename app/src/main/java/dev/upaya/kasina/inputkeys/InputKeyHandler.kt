package dev.upaya.kasina.inputkeys

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InputKeyHandler @Inject constructor() {

    private val button = PressableButton()
    val buttonState = button.buttonState

    fun handleButtonPress(scope: CoroutineScope) {
        button.press(scope = scope)
    }

    fun handleButtonRelease() {
        button.release()
    }
}
