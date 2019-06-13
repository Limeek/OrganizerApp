package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.databinding.FragmentCalendarEventsBinding
import ru.limeek.organizer.di.components.ViewViewModelComponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.EventsViewModel
import java.util.*
import javax.inject.Inject

class EventsFragment : Fragment(){
    val logTag = "EventsFragment"

    @Inject
    lateinit var viewModel: EventsViewModel

    private lateinit var binding: FragmentCalendarEventsBinding

    var adapter = EventsListAdapter()
    private var component : ViewViewModelComponent? = null

    private val adapterOnClick = fun(event: Event){
        startDetailsActivity(event)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCalendarEventsBinding.inflate(inflater ,container,false)
        binding.lifecycleOwner = this
        getViewComponent().inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        adapter.onItemClick = adapterOnClick
        binding.recAdapter = adapter
        viewModel.refresh()
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.startDetailsActivity.observe(this, Observer{
            startDetailsActivity()
        })
    }

    private fun startDetailsActivity(event: Event? = null) {
        val intent = Intent(activity, EventDetailsActivity::class.java)
        if(event != null){
            val bundle = Bundle()
            bundle.putParcelable("event", event)
            bundle.putBoolean("uneditable", true)
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