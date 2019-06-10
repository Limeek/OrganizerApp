package ru.limeek.organizer.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.views.LocationsAdapter
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class LocationsAdapterPresenter(var locationsAdapter: LocationsAdapter) {
    var locations = listOf<Location>()
    var disposable: Disposable? = null

    @Inject
    lateinit var repository: LocationRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
        updateLocations()
    }

    fun updateLocations() {
        disposable =
                repository.getUserCreatedLocations()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { locations ->
                            this.locations = locations
                            locationsAdapter.notifyDataSetChanged()
                        }
    }

    fun getCount(): Int {
        return locations.size
    }

    fun getItemAt(position: Int): Location {
        return locations[position]
    }

    fun onDestroy() {
        disposable!!.dispose()
        disposable = null
    }
}