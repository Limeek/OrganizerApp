package ru.limeek.organizer.views

import android.widget.LinearLayout
import android.widget.TextView
import com.thbs.skycons.library.SkyconView

interface WeatherFragmentView {
    var degrees : TextView
    var description : TextView
    var iconLayout : LinearLayout
    var icon : SkyconView
    var city : TextView

    fun setWeatherIcon(iconString : String)
}