package ru.limeek.organizer.presenters

import android.view.View

interface EventsPresenter {
    fun onCreate()
    fun onFabClick() : View.OnClickListener
    fun onDestroy()
    fun getCurrentDateString(): String
}