package ru.limeek.organizer.viewmodels

import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.weather.WeatherInfo
import ru.limeek.organizer.data.repository.WeatherRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WeatherViewModel @Inject constructor(private var weatherRepo: WeatherRepository) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext= Dispatchers.IO + SupervisorJob()

    var weatherInfo = MutableLiveData<WeatherInfo>()
    var city = MutableLiveData<String>()
    var iconString: LiveData<String> = Transformations.map(weatherInfo) { input -> input?.currently?.icon?:"" }

    lateinit var location: Location

    fun onCreate() {
        if (App.instance.checkLocationPermission()) {
            val locationManager = App.instance.locationManager
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
            getWeather()
        }
    }

    private fun getWeather() {
        launch {
            val wInfo = weatherRepo.getWeatherForLocation(getLocationString())
            weatherInfo.postValue(wInfo)
            setupCityName()
        }
    }

    private fun getLocationString(): String {
        return "${location.latitude},${location.longitude}"
    }

    private fun setupCityName() {
        val geoCoder = Geocoder(App.instance.applicationContext)
        val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        city.postValue(address[0].locality.toString())
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext[Job]!!.cancel()
    }

}