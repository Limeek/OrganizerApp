package ru.limeek.organizer.data.repository

import io.reactivex.Flowable
import ru.limeek.organizer.data.api.DarkSkyApi
import ru.limeek.organizer.data.model.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepository @Inject constructor(var darkSkyApi: DarkSkyApi) {
    fun getWeatherForLocation(location: String?): Flowable<WeatherInfo>{
        return darkSkyApi.getWeatherForLocation(location)
    }
}