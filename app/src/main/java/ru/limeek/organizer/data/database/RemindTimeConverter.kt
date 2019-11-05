package ru.limeek.organizer.data.database

import androidx.room.TypeConverter
import ru.limeek.organizer.domain.entities.event.RemindTime

object RemindTimeConverter {
    @TypeConverter
    @JvmStatic
    fun toRemindTime(value: Long?) : RemindTime?{
        RemindTime.values().forEach {
            if(value == it.millis)
                return it
        }
        return null
    }

    @TypeConverter
    @JvmStatic
    fun toString(remindTime: RemindTime) : Long?{
        return remindTime.millis
    }
}