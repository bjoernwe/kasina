package dev.upaya.kasina.data

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.upaya.kasina.flashlight.FlashlightStateController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionStateRepository @Inject constructor(
    @ApplicationContext appContext: Context,
    flashlightStateController: FlashlightStateController,
) {

    val sessionState = flashlightStateController.flashlightState.toSessionState()

    private val db = Room.databaseBuilder(
        appContext,
        RoomDatabase::class.java, "kasina"
    ).build()

    private val sessionDao = db.sessionDao()

    // We could emit sessions directly, but this way the DB is the single source of truth
    val recentSessions = sessionDao.recentSessions(limit = 15, minLength = 5000)

    private val _currentSession = MutableStateFlow<Session?>(null)
    val currentSession: StateFlow<Session?> = _currentSession

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    suspend fun startStoringSessions(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(dispatcher) {
            launchJobForCurrentSessionState()
            launchJobForStoringSessions()
            launchJobForFirebaseAnalytics()
        }
    }

    private fun CoroutineScope.launchJobForCurrentSessionState() {
        launch {
            sessionState.collect { sessionState ->
                _currentSession.update { session ->
                    session.createOrUpdate(sessionState)
                }
            }
        }
    }

    private fun CoroutineScope.launchJobForStoringSessions() = launch {
        _currentSession.collect {
            storeSession(it)
        }
    }

    private fun CoroutineScope.launchJobForFirebaseAnalytics() = launch {
        _currentSession.collect { session ->
            if (session == null || session.isIncomplete)
                return@collect
            firebaseAnalytics.logEvent("session") {
                param("duration_on", session.onDuration.toDouble())
                param("duration_off", session.offDuration.toDouble())
            }
        }
    }

    private suspend fun storeSession(session: Session?) {
        if (session == null || session.isIncomplete) return
        sessionDao.insert(session)
    }

    companion object {

        private fun Session?.createOrUpdate(sessionState: SessionState): Session {
            if (this == null)
                return Session(timestampFlash = Instant.now().toEpochMilli())
            return when (sessionState) {
                SessionState.ACTIVE_ON -> { Session(timestampFlash = Instant.now().toEpochMilli()) }
                SessionState.ACTIVE_OFF -> { this.copy(timestampStart = Instant.now().toEpochMilli()) }
                SessionState.INACTIVE -> { this.copy(timestampEnd = Instant.now().toEpochMilli()) }
            }
        }

    }
}
