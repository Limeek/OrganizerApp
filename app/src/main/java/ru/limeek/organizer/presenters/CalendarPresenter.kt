package ru.limeek.organizer.presenters

import android.widget.CalendarView

interface CalendarPresenter {
    fun onDateChange(day: Int, month: Int, year: Int)
    fun onCreate()
}