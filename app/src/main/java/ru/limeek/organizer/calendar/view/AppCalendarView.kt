package ru.limeek.organizer.calendar.view

import android.widget.TextView
import ru.limeek.organizer.mvp.View

interface AppCalendarView : View {
    var currentDate : TextView

    fun refreshEventsFragment()
    fun setDate(millis : Long)
}