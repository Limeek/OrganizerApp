package ru.limeek.organizer.database

import androidx.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.util.Constants

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun fromDate(dateTime: DateTime) : String {
        return dateTime.toString(DateTimeFormat.forPattern(Constants.FORMAT_YY_MM_dd))
    }

    @TypeConverter
    @JvmStatic
    fun toDate(value : String) : DateTime{
        return DateTime.parse(value)
    }
}