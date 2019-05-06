package ru.limeek.organizer.digest.weather.presenter

import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.digest.weather.view.WeatherFragmentView
import ru.limeek.organizer.model.weather.WeatherInfo
import ru.limeek.organizer.repository.WeatherRepository
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherPresenterImpl(private val weatherFragmentView: WeatherFragmentView) : WeatherPresenter {
    lateinit var weatherInfo: WeatherInfo
    lateinit var city: String
    lateinit var location: Location
    var disposable: Disposable? = null

    @Inject
    lateinit var repository: WeatherRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun onCreate() {
        if (App.instance.checkLocationPermission()) {
            val locationManager = App.instance.locationManager
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
            getWeather()
        }
    }

    override fun updateUI() {
        weatherFragmentView.degrees.text = weatherInfo.currently!!.temperature!!.roundToInt().toString()
        weatherFragmentView.description.text = weatherInfo.currently!!.summary.toString()
        weatherFragmentView.city.text = city
        weatherFragmentView.setWeatherIcon(weatherInfo.currently!!.icon!!)
    }

    private fun getWeather() {
        disposable =
                repository
                        .getWeatherForLocation(getLocationString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { weatherInfo ->
                            this.weatherInfo = weatherInfo
                            setupCityName()
                            updateUI()
                        }
    }

    private fun getLocationString(): String {
        return "${location.latitude},${location.longitude}"
    }

    private fun setupCityName() {
        val geoCoder = Geocoder(App.instance.applicationContext)
        val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        city = address[0].locality.toString()
    }

    override fun onDestroy() {
        disposable!!.dispose()
        disposable = null
    }

}