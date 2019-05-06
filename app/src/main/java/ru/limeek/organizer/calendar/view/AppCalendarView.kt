package ru.limeek.organizer.calendar.view

import android.widget.TextView
import ru.limeek.organizer.mvp.View

interface AppCalendarView : View {
    fun refreshEventsFragment()
    fun setDate(millis : Long)

    fun updateCurrentDate(dateString: String)
}