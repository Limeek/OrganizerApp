package ru.limeek.organizer.repository

import io.reactivex.Flowable
import ru.limeek.organizer.api.DarkSkyApi
import ru.limeek.organizer.model.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepository @Inject constructor(var darkSkyApi: DarkSkyApi) {
    fun getWeatherForLocation(location: String?): Flowable<WeatherInfo>{
        return darkSkyApi.getWeatherForLocation(location)
    }
}