package dev.upaya.kasina

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.upaya.kasina.ui.MainScreen
import dev.upaya.kasina.ui.theme.FlashKasinaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val volumeKeys = setOf(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP)
    private lateinit var inputKeyHandlerResource: InputKeyHandlerResource
    private lateinit var flashLightControllerResource: FlashLightControllerResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashKasinaTheme(darkTheme = true) {
                MainScreen()
            }
        }
    }

    private fun onVolumeDownShortPress() {
        flashLightControllerResource.toggle()
    }

    private fun onVolumeDownLongPress(scope: CoroutineScope) {
        scope.launch {
            flashLightControllerResource.turnOnFor(timeMillis = 1000)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event)

        if (keyCode in volumeKeys) {
            inputKeyHandlerResource.handleVolumeDownPress(
                shortPressCallback = { onVolumeDownShortPress() },
                longPressCallback = { onVolumeDownLongPress(lifecycleScope) },
                scope = lifecycleScope,
            )
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode in volumeKeys) {
            inputKeyHandlerResource.handleVolumeDownRelease()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onStart() {
        super.onStart()
        inputKeyHandlerResource = InputKeyHandlerResource(this)
        flashLightControllerResource = FlashLightControllerResource(this)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        inputKeyHandlerResource.close()
        flashLightControllerResource.close()
    }
}
