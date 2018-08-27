package ru.limeek.organizer.event.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.event.eventdetails.view.EventDetailsActivity
import ru.limeek.organizer.event.presenter.EventsPresenter
import javax.inject.Inject

class EventsFragment : EventFragmentView, Fragment(){

    val logTag = "EventsFragment"

    lateinit var recView : RecyclerView
    var recAdapter : EventsListAdapter? = null
    lateinit var fab : FloatingActionButton
    @Inject
    lateinit var presenter : EventsPresenter

    private var component : ViewComponent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.fragment_calendar_events,container,false)

        getViewComponent().inject(this)

        recAdapter = EventsListAdapter(context)
        recView = view.findViewById(R.id.recViewEvents)
        recView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context,VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = recAdapter
        }

        fab = view.findViewById(R.id.floatingButton)
        fab.setOnClickListener(presenter.onFabClick())

        return view
    }

    override fun startDetailsActivity() {
        val intent = Intent(activity, EventDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun refreshRecyclerView() {
        recAdapter!!.refreshRecycler()
    }

    private fun getViewComponent() : ViewComponent{
        if(component == null){
            component = App.instance.component.newViewComponent(PresenterModule(this))
        }
        return component!!
    }

    override fun onDestroy() {
        super.onDestroy()
        recAdapter!!.onDestroy()
        presenter.onDestroy()
        component = null
    }
}