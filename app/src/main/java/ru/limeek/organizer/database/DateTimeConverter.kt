package ru.limeek.organizer.database

import androidx.room.TypeConverter
import org.joda.time.DateTime

object DateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun toDateTime(value : String?) : DateTime{
        return DateTime.parse(value)
    }

    @TypeConverter
    @JvmStatic
    fun fromDateTime(date: DateTime) : String{
        return date.toString()
    }
}