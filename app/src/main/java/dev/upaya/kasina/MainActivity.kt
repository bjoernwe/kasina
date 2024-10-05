package dev.upaya.kasina

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BUTTON_A
import android.view.KeyEvent.KEYCODE_BUTTON_B
import android.view.KeyEvent.KEYCODE_BUTTON_L1
import android.view.KeyEvent.KEYCODE_BUTTON_L2
import android.view.KeyEvent.KEYCODE_BUTTON_R1
import android.view.KeyEvent.KEYCODE_BUTTON_R2
import android.view.KeyEvent.KEYCODE_BUTTON_X
import android.view.KeyEvent.KEYCODE_BUTTON_Y
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.view.KeyEvent.KEYCODE_HEADSETHOOK
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.KeyEvent.KEYCODE_VOLUME_UP
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
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

    private val inputKeys = setOf(
        KEYCODE_VOLUME_DOWN,
        KEYCODE_VOLUME_UP,
        KEYCODE_DPAD_UP,
        KEYCODE_DPAD_DOWN,
        KEYCODE_DPAD_LEFT,
        KEYCODE_DPAD_RIGHT,
        KEYCODE_BUTTON_A,
        KEYCODE_BUTTON_B,
        KEYCODE_BUTTON_X,
        KEYCODE_BUTTON_Y,
        KEYCODE_BUTTON_L1,
        KEYCODE_BUTTON_L2,
        KEYCODE_BUTTON_R1,
        KEYCODE_BUTTON_R2,
        KEYCODE_HEADSETHOOK,
    )

    private lateinit var flashlightViewModel: FlashlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)

        flashlightViewModel = ViewModelProvider(this)[FlashlightViewModel::class.java]

        setContent {
            FlashKasinaTheme(darkTheme = true, dynamicColor = false) {
                MainScreen()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event)

        if (keyCode in inputKeys) {
            flashlightViewModel.handleButtonPress(lifecycleScope)
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode in inputKeys) {
            flashlightViewModel.handleButtonRelease()
            return true
        }

        return super.onKeyUp(keyCode, event)
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
