package ru.limeek.organizer.presenters

import android.os.Bundle
import com.google.android.gms.location.places.Place
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.views.LocationDetailsView
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class LocationDetailsPresenterImpl (var locationDetailsView: LocationDetailsView?) : LocationDetailsPresenter {
    var location : Location? = null
    var disposable: Disposable? = null

    @Inject
    lateinit var locationRepository: LocationRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun delete() {
        disposable =
                locationRepository
                    .deleteLocation(location!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe()
    }

    override fun submit() {
        disposable =
                Observable.fromCallable{
                    if(locationDetailsView!!.getLocation() == null) {
                        location!!.name = locationDetailsView!!.getName()
                        val locationId = locationRepository.insert(location!!)
                        if (locationDetailsView!!.getFromEventDetails() != null) {
                            val bundle = Bundle()
                            bundle.putLong("locationId",locationId)
                            locationDetailsView!!.startEventDetailsWithResult(bundle)
                        }
                        else locationDetailsView!!.startLocationActivity()
                    }
                    else{
                        val locationName = locationDetailsView!!.getAddress()
                        val updatedLocation = Location(location!!.id, location!!.latitude, location!!.longitude, locationName, location!!.address, location!!.createdByUser)
                        locationRepository.update(updatedLocation)
                        locationDetailsView!!.startLocationActivity()
                    }
                }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe()
    }

    override fun createLocation(place : Place) {
        location = if (location == null)
            Location(place.latLng.latitude, place.latLng.longitude, place.name.toString(), place.address.toString(), true)
        else
            Location(location!!.id, place.latLng.latitude, place.latLng.longitude, place.name.toString(), place.address.toString(), true)
    }

    private fun updateUI(){
        locationDetailsView!!.updateName(location!!.name)
        locationDetailsView!!.updateAddress(location!!.address)
    }

    override fun onCreate() {
        location = locationDetailsView!!.getLocation()
        if(location != null) updateUI()
    }

    override fun onDestroy() {
        if(disposable != null) {
            disposable!!.dispose()
            disposable = null
        }
    }
}