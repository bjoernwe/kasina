package dev.upaya.kasina.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    version = 1,
    exportSchema = true,
    entities = [FlashlightEventEntity::class],
)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun flashlightEventDao(): FlashlightEventDao
}
