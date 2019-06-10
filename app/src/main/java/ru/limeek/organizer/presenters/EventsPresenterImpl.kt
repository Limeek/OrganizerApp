package ru.limeek.organizer.presenters

import android.view.View
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.views.EventFragmentView
import javax.inject.Inject

class EventsPresenterImpl(var eventFragmentView: EventFragmentView?) : EventsPresenter {
    val logTag = "EventsPresenter"

    @Inject
    lateinit var sharedPrefRepo: SharedPrefsRepository

    override fun onCreate() {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun onFabClick(): View.OnClickListener {
        return View.OnClickListener {
            eventFragmentView!!.startDetailsActivity()
        }
    }

    override fun getCurrentDateString(): String{
        return sharedPrefRepo.getDateString("cachedDate")
    }


    override fun onDestroy() {
        eventFragmentView = null
    }
}
