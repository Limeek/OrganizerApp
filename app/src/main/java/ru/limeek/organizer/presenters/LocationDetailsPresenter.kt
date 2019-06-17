package ru.limeek.organizer.presenters


interface LocationDetailsPresenter {
    fun delete()
    fun submit()
//    fun createLocation(place: Place)
    fun onCreate()
    fun onDestroy()
}