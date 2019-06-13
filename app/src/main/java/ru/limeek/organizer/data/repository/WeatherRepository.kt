package ru.limeek.organizer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.api.DarkSkyApi
import ru.limeek.organizer.data.model.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepository @Inject constructor(var darkSkyApi: DarkSkyApi) {
    suspend fun getWeatherForLocation(location: String?): WeatherInfo?{
        return withContext(Dispatchers.IO){
            darkSkyApi.getWeatherForLocation(location)
        }
    }
}