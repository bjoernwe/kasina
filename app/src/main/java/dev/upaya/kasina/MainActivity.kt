package dev.upaya.kasina

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.upaya.kasina.flashlight.FlashlightViewModel
import dev.upaya.kasina.ui.MainScreen
import dev.upaya.kasina.ui.theme.FlashKasinaTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var flashlightViewModel: FlashlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        flashlightViewModel = ViewModelProvider(this)[FlashlightViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            FlashKasinaTheme(darkTheme = true, dynamicColor = false) {
                MainScreen()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event)

        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> { flashlightViewModel.handleVolumeDownPress(lifecycleScope); true }
            KeyEvent.KEYCODE_VOLUME_UP -> { flashlightViewModel.handleVolumeUpPress(lifecycleScope); true }
            else -> { super.onKeyDown(keyCode, event) }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> { flashlightViewModel.handleVolumeDownRelease(); true }
            KeyEvent.KEYCODE_VOLUME_UP -> { flashlightViewModel.handleVolumeUpRelease(); true }
            else -> { super.onKeyUp(keyCode, event) }
        }
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
        if (!isChangingConfigurations) {
            flashlightViewModel.turnFlashOff()
        }
    }
}
