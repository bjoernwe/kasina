package dev.upaya.kasina.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FlashlightEventDao {

    @Insert
    suspend fun insert(vararg events: FlashlightEventEntity)

    @Query("SELECT * FROM flashlight_events ORDER BY timestamp DESC LIMIT :limit")
    fun recentEvents(limit: Int = 1): Flow<List<FlashlightEventEntity>>
}
