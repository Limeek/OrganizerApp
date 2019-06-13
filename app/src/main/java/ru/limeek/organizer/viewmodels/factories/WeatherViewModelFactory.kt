package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.WeatherRepository
import ru.limeek.organizer.viewmodels.WeatherViewModel
import javax.inject.Inject

class WeatherViewModelFactory @Inject constructor(private var weatherRepo: WeatherRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            return WeatherViewModel(weatherRepo) as T
        }
        throw IllegalStateException()
    }
}