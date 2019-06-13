package ru.limeek.organizer.viewmodels

import androidx.lifecycle.ViewModel
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository

class EventDetailsViewModel(private var sharedPresRepo: SharedPrefsRepository,
                            private var eventRepo: EventRepository,
                            private var locationRepo: LocationRepository) : ViewModel(){

}