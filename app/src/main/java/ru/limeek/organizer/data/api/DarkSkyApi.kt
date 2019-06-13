package ru.limeek.organizer.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.limeek.organizer.data.model.weather.WeatherInfo

interface DarkSkyApi {
    @GET("{location}?units=si")
    suspend fun getWeatherForLocation(@Path("location") location: String?) : WeatherInfo?
}
