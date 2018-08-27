package ru.limeek.organizer.calendar.presenter

import android.widget.CalendarView

interface CalendarPresenter {
    fun onDateChange(): CalendarView.OnDateChangeListener
    fun onCreate()
}