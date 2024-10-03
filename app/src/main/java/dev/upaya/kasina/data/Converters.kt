package dev.upaya.kasina.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class Converters {

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    @TypeConverter
    fun stringToOffsetDateTime(value: String?): OffsetDateTime? {
        return try {
            value?.let { OffsetDateTime.parse(value) }
        } catch (e: DateTimeParseException) {
            null
        }
    }

    @TypeConverter
    fun offsetDateTimeToString(date: OffsetDateTime?): String? {
        if (date == null) {
            return null
        }
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(date)
    }

    @TypeConverter
    fun stringToDate(value: String?): LocalDate? {
        return try {
            value?.let { LocalDate.parse(value, dateFormatter) }
        } catch (e: DateTimeParseException) {
            null
        }
    }
}
