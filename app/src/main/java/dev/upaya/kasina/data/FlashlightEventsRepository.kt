package dev.upaya.kasina.data

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.upaya.kasina.flashlight.FlashlightEvent
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlashlightEventsRepository @Inject constructor(
    @ApplicationContext appContext: Context
) {

    private val db = Room.databaseBuilder(
        appContext,
        RoomDatabase::class.java, "kasina"
    ).build()

    private val flashlightEventDao = db.flashlightEventDao()

    val recentEvents = flashlightEventDao.recentEvents(3).map { events ->
        events.map { it.toFlashlightEvent() }
    }

    suspend fun storeEvent(event: FlashlightEvent) {
        flashlightEventDao.insert(
            FlashlightEventEntity.fromFlashlightEvent(event)
        )
    }
}
