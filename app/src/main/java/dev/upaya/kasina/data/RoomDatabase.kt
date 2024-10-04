package dev.upaya.kasina.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    version = 1,
    exportSchema = true,
    entities = [Session::class],
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
