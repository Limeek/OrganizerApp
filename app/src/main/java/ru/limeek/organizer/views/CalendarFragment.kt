package ru.limeek.organizer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.calendar_fragment.*
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.presenters.CalendarPresenter
import javax.inject.Inject

class CalendarFragment: AppCalendarView,Fragment() {
    @Inject
    lateinit var presenter: CalendarPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.component.newViewComponent(PresenterModule(this)).inject(this)
        presenter.onCreate()
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            presenter.onDateChange(dayOfMonth, month, year)
            (activity as? MainActivity)?.refreshEventsFragment()
        }
    }

    override fun setDate(millis: Long) {
        calendarView.setDate(millis,false,true)
    }
}