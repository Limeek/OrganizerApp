package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_calendar_events.*
import ru.limeek.organizer.R
import ru.limeek.organizer.adapter.EventsListAdapter
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.di.components.ViewViewModelComponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.viewmodels.EventsViewModel
import javax.inject.Inject

class EventsFragment : Fragment(){
    val logTag = "EventsFragment"

    @Inject
    lateinit var viewModel: EventsViewModel

    private var component : ViewViewModelComponent? = null

    private val adapter: EventsListAdapter by lazy {
        EventsListAdapter().apply {
            recViewEvents.adapter = this
            onItemClick = adapterOnClick
        }
    }

    private val adapterOnClick = fun(event: Event){
        startDetailsActivity(event)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_calendar_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewComponent().inject(this)
        viewModel.refresh()
        observeLiveData()
        floatingButton.setOnClickListener { startDetailsActivity() }
    }

    private fun observeLiveData(){
        viewModel.startDetailsActivity.observe(viewLifecycleOwner, Observer{
            startDetailsActivity()
        })

        viewModel.events.observe(viewLifecycleOwner, Observer{
            adapter.dataset = it
            adapter.notifyDataSetChanged()
        })
    }

    private fun startDetailsActivity(event: Event? = null) {
        val intent = Intent(activity, EventDetailsActivity::class.java)
        if(event != null){
            val bundle = Bundle()
            bundle.putParcelable(Constants.EVENT, event)
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    private fun getViewComponent() : ViewViewModelComponent{
        if(component == null){
            component = App.instance.component.newViewViewModelComponent(ViewModelModule(this))
        }
        return component!!
    }


    override fun onResume() {
        super.onResume()
        refresh()
    }

    fun refresh(){
        viewModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        component = null
    }
}