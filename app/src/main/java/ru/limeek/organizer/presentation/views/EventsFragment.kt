package ru.limeek.organizer.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import ru.limeek.organizer.R
import ru.limeek.organizer.databinding.FragmentCalendarEventsBinding
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.presentation.adapter.EventsListAdapter
import ru.limeek.organizer.presentation.viewmodels.EventsViewModel
import javax.inject.Inject

class EventsFragment : DaggerFragment(){
    @Inject
    lateinit var viewModel: EventsViewModel

    private lateinit var binding: FragmentCalendarEventsBinding

    private val adapter: EventsListAdapter by lazy {
        EventsListAdapter().apply {
            onItemClick = adapterOnClick
        }
    }

    private val adapterOnClick = fun(event: Event){
        startDetailsActivity(event)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCalendarEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refresh()
        observeLiveData()
        initRecycler()
    }

    private fun initRecycler(){
        binding.recViewEvents.adapter = adapter
        binding.recViewEvents.layoutManager = LinearLayoutManager(context)
    }

    private fun observeLiveData(){
        viewModel.events.observe(viewLifecycleOwner, Observer{
            binding.tvEventsCount.text = getString(R.string.events_count, it.size)
            adapter.dataset = it
            adapter.notifyDataSetChanged()
        })

        viewModel.currentDateString.observe(viewLifecycleOwner, Observer {
            binding.tvCurrentDate.text = it
        })
    }

    private fun startDetailsActivity(event: Event? = null) {
        (parentFragment as? MainFragment)?.startEventDetailsFragment(event)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    fun refresh(){
        viewModel.refresh()
    }
}