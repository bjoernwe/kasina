package dev.upaya.kasina.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


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

    val onDuration: Long
        get() = if (timestampStart == null) 0 else (timestampStart - timestampFlash)

    val offDuration: Long
        get() = if (timestampStart == null || timestampEnd == null) 0 else (timestampEnd - timestampStart)
}
