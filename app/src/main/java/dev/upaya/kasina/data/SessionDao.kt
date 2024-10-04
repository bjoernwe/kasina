package dev.upaya.kasina.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SessionDao {

    @Insert
    suspend fun insert(vararg sessions: Session)

    @Query("SELECT * FROM sessions ORDER BY timestamp_flash DESC LIMIT :limit")
    fun recentSessions(limit: Int = 1): Flow<List<Session>>
}
