package dev.upaya.kasina

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Vibrator @Inject constructor(
    @ApplicationContext appContext: Context
) {

    companion object {

        private val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)

        fun getVibrator(context: Context): Vibrator? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getVibratorNew(context)
            } else {
                getVibratorOld(context)
            }
        }

        @RequiresApi(Build.VERSION_CODES.S)
        private fun getVibratorNew(context: Context): Vibrator? {
            val vibratorManager: VibratorManager
            try {
                vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            } catch (e: NullPointerException) {
                return null
            }
            return vibratorManager.defaultVibrator
        }

        private fun getVibratorOld(context: Context): Vibrator? {
            val vibrator: Vibrator
            try {
                @Suppress("DEPRECATION")
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            } catch (e: NullPointerException) {
                return null
            }
            return vibrator
        }
    }

    private val vibrator = getVibrator(appContext)

    fun vibrate() {
        vibrator?.vibrate(vibrationEffect)
    }
}
