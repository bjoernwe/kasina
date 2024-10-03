package dev.upaya.kasina.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.upaya.kasina.flashlight.FlashlightEvent
import java.time.OffsetDateTime
import java.time.ZoneId


@Entity(tableName = "flashlight_events")
data class FlashlightEventEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "is_on")
    val isOn: Boolean,

    @ColumnInfo(name = "timestamp", index = true)
    val date: OffsetDateTime = OffsetDateTime.now(),
) {

    companion object {
        fun fromFlashlightEvent(event: FlashlightEvent): FlashlightEventEntity {
            return FlashlightEventEntity(
                isOn = event.isOn,
                date = event.timestamp.atZone(ZoneId.systemDefault()).toOffsetDateTime()
            )
        }
    }

    fun toFlashlightEvent(): FlashlightEvent {
        return FlashlightEvent(
            isOn = isOn,
            timestamp = date.toInstant()
        )
    }
}
