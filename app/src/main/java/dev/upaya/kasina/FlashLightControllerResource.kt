package dev.upaya.kasina

import android.content.Context
import kotlinx.coroutines.delay


class FlashLightControllerResource(context: Context) : AutoCloseable {

    private val flashLightResource = FlashLightResource(context)

    fun toggle() {
        flashLightResource.toggle()
    }

    suspend fun turnOnFor(timeMillis: Long) {

        if (flashLightResource.isOn)
            return

        flashLightResource.turnOn()
        waitAndTurnOff(timeMillis)
    }

    private suspend fun waitAndTurnOff(timeMillis: Long) {
        try {
            delay(timeMillis)
        } finally {
            flashLightResource.turnOff()
        }
    }

    override fun close() {
        flashLightResource.close()
    }
}
