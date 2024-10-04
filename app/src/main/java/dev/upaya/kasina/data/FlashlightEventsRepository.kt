package dev.upaya.kasina.data

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import java.util.LinkedList
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

    val recentSessions = flashlightEventDao.recentEvents(10)
        .map { events ->
            events.map { it.toFlashlightEvent() }
        }.map { events ->
            events.reversed()
        }.map { events ->
            calcSessions(events)
        }.map { sessions ->
            sessions.reversed()
        }

    suspend fun storeEvent(event: FlashlightEvent) {
        flashlightEventDao.insert(
            FlashlightEventEntity.fromFlashlightEvent(event)
        )
    }

    companion object {

        fun calcSessions(events: List<FlashlightEvent>): List<Session> {

            val result = ArrayList<Session>()
            val linkedEvents = LinkedList(events)

            var i = 0

            while (linkedEvents.has(i+2)) {

                // remove leading OFF events
                while (linkedEvents.has(i) && linkedEvents[i].isOff)
                    linkedEvents.removeAt(i)

                // remove redundant ON events
                while (linkedEvents.has(i+1) && linkedEvents[i+1].isOn)
                    linkedEvents.removeAt(i+1)

                // remove redundant OF events
                while (linkedEvents.has(i+2) && linkedEvents[i+2].isOff)
                    linkedEvents.removeAt(i+2)

                // add session (if possible)
                if (linkedEvents.has(i+2))
                    result.add(
                        Session(
                            onDuration = linkedEvents[i+1].timestamp.epochSecond - linkedEvents[i].timestamp.epochSecond,
                            offDuration = linkedEvents[i+2].timestamp.epochSecond - linkedEvents[i+1].timestamp.epochSecond,
                        )
                    )

                i += 2
            }

            return result
        }

        private fun List<FlashlightEvent>.has(i: Int): Boolean {
            return i < size
        }
    }

}
