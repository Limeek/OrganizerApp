package ru.limeek.organizer.presenters

import com.google.android.gms.location.places.Place

interface LocationDetailsPresenter {
    fun delete()
    fun submit()
    fun createLocation(place: Place)
    fun onCreate()
    fun onDestroy()
}