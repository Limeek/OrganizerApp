package ru.limeek.organizer.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.views.LocationsAdapter
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocationsAdapterPresenter(var locationsAdapter: LocationsAdapter) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()
    var locations = listOf<Location>()

    @Inject
    lateinit var repository: LocationRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
        updateLocations()
    }

    private fun updateLocations() {
        launch {
            this@LocationsAdapterPresenter.locations = repository.getUserCreatedLocations()
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
        coroutineContext[Job]!!.cancel()
    }
}