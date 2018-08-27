package ru.limeek.organizer.digest.weather.presenter

interface WeatherPresenter {
    fun updateUI()
    fun onCreate()
    fun onDestroy()
}