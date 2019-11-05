package ru.limeek.organizer.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.calendar_fragment.*
import ru.limeek.organizer.R
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.di.modules.ViewModelModule
import ru.limeek.organizer.presentation.viewmodels.CalendarViewModel
import javax.inject.Inject

class CalendarFragment: Fragment() {

    @Inject
    lateinit var viewModel: CalendarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        App.instance.component.newViewComponent(ViewModelModule(this)).inject(this)
        viewModel.onCreate()
        observeLiveData()
    }

    private fun initView(){
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.onDateChange(year, month, dayOfMonth)
        }
    }

    private fun observeLiveData(){
        viewModel.refreshEventFragment.observe(viewLifecycleOwner, Observer{
            (activity as? MainActivity)?.refreshEventsFragment()
        })
        viewModel.currDate.observe(viewLifecycleOwner, Observer {
            calendarView.date = it
        })
    }
}