package ru.limeek.organizer.model

import android.content.SharedPreferences
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.util.Constants
import javax.inject.Inject

class OrganizerSharedPreferences @Inject constructor(var sharedPreferences: SharedPreferences) {

    fun putDateTime(key: String, data: DateTime){
        sharedPreferences.edit().putString(key,data.toString()).apply()
    }

    fun putDataString(key: String, data: String){
        sharedPreferences.edit().putString(key,data).apply()
    }

    fun getDataString(key: String) : String{
        return sharedPreferences.getString(key, "")
    }

    fun getDateString(key : String) : String{
        val dateTime = DateTime.parse(sharedPreferences.getString(key,""))
        return dateTime.toString(DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY))
    }

    fun getDateTime(key : String) : DateTime{
        return DateTime.parse(sharedPreferences.getString(key,""))
    }
}