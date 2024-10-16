package dev.upaya.kasina.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SessionDao {

    @Insert
    suspend fun insert(vararg sessions: Session)

    @Query("SELECT * FROM sessions WHERE (timestamp_end - timestamp_flash) >= :minLength ORDER BY timestamp_flash DESC LIMIT :limit")
    fun recentSessions(limit: Int = 1, minLength: Long = 1000): Flow<List<Session>>
}
