package dev.upaya.kasina.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant


@Entity(tableName = "sessions")
data class Session(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "timestamp_flash", index = true)
    val timestampFlash: Long,

    @ColumnInfo(name = "timestamp_start")
    val timestampStart: Long? = null,

    @ColumnInfo(name = "timestamp_end")
    val timestampEnd: Long? = null,

) {
    val isIncomplete: Boolean
        get() = timestampStart == null || timestampEnd == null

    val onDuration: Float
        get() = if (timestampStart == null) 0f else (timestampStart - timestampFlash).div(1000f)

    val offDuration: Float
        get() = if (timestampStart == null || timestampEnd == null) 0f else (timestampEnd - timestampStart).div(1000f)

    val start: Instant?
        get() = timestampStart?.let { Instant.ofEpochMilli(it) }
}
