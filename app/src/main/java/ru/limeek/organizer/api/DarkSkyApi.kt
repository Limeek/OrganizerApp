package ru.limeek.organizer.api

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import ru.limeek.organizer.model.weather.WeatherInfo

interface DarkSkyApi {
    @GET("{createdCustomLocation}?units=si")
    fun getWeatherForLocation(@Path("createdCustomLocation") location: String?) : Flowable<WeatherInfo>
}