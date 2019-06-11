package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_calendar_events.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.databinding.FragmentCalendarEventsBinding
import ru.limeek.organizer.databinding.FragmentCalendarEventsBindingImpl
import ru.limeek.organizer.di.components.ViewComponent
import ru.limeek.organizer.di.components.ViewViewModelComponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.viewmodels.EventsViewModel
import javax.inject.Inject

class EventsFragment : EventFragmentView, Fragment(){
    val logTag = "EventsFragment"




    @Inject
    lateinit var viewModel: EventsViewModel
    private lateinit var binding: FragmentCalendarEventsBinding

    var adapter : EventsListAdapter? = null
    private var component : ViewViewModelComponent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCalendarEventsBinding.inflate(inflater ,container,false)
        getViewComponent().inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = EventsListAdapter()
        binding.recViewEvents.apply {
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
            adapter = adapter
        }
        refreshDateText()
    }

    override fun startDetailsActivity() {
        val intent = Intent(activity, EventDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun refreshRecyclerView() {
        adapter!!.refreshRecycler()
    }

    private fun getViewComponent() : ViewViewModelComponent{
        if(component == null){
            component = App.instance.component.newViewViewModelComponent(ViewModelModule())
        }
        return component!!
    }

    private fun refreshDateText(){
        tvCurrentDate.text = viewModel.getCurrentDateString()
    }

    fun refresh(){
        refreshRecyclerView()
        refreshDateText()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter!!.onDestroy()
        component = null
    }
}