package ru.limeek.organizer.api

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import ru.limeek.organizer.model.weather.WeatherInfo

interface DarkSkyApi {
    @GET("{location}?units=si")
    fun getWeatherForLocation(@Path("location") location: String?) : Flowable<WeatherInfo>
}
