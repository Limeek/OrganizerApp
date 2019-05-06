package ru.limeek.organizer.database

import android.arch.persistence.room.TypeConverter
import ru.limeek.organizer.model.event.RemindTime

object RemindTimeConverter {
    @TypeConverter
    @JvmStatic
    fun toRemindTime(value : String?) : RemindTime?{
        RemindTime.values().forEach {
            if(value.equals(it.durationName))
                return it
        }
        return null
    }

    @TypeConverter
    @JvmStatic
    fun toString(remindTime: RemindTime) : String?{
        return remindTime.durationName
    }
}