package ru.limeek.organizer.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import dagger.android.support.DaggerFragment
import ru.limeek.organizer.databinding.CalendarFragmentBinding
import ru.limeek.organizer.presentation.viewmodels.CalendarViewModel
import javax.inject.Inject

class CalendarFragment: DaggerFragment() {

    @Inject
    lateinit var viewModel: CalendarViewModel

    private lateinit var binding: CalendarFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.onCreate()
        observeLiveData()
    }

    private fun initView(){
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.onDateChange(year, month, dayOfMonth)
        }
    }

    private fun observeLiveData(){
        viewModel.refreshEventFragment.observe(viewLifecycleOwner, Observer{
            (parentFragment as? MainFragment)?.refreshEventsFragment()
        })
        viewModel.currDate.observe(viewLifecycleOwner, Observer {
            binding.calendarView.date = it
        })
    }
}