package ru.limeek.organizer.event.presenter

import android.view.View

interface EventsPresenter {
    fun onCreate()
    fun onFabClick() : View.OnClickListener
    fun onDestroy()
}