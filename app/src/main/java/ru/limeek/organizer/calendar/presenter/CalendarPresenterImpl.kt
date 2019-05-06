package ru.limeek.organizer.calendar.presenter

import android.widget.CalendarView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.app.App
import ru.limeek.organizer.calendar.view.AppCalendarView
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.repository.SharedPrefsRepository
import ru.limeek.organizer.util.Constants
import javax.inject.Inject


class CalendarPresenterImpl(var calView: AppCalendarView?) : CalendarPresenter {
    private val logTag = "CalendarPresenterImpl"

    @Inject
    lateinit var repository: SharedPrefsRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun onDateChange(): CalendarView.OnDateChangeListener {
        return CalendarView.OnDateChangeListener { _: CalendarView, year: Int, month: Int, day: Int ->
            val cachedDate = DateTime.parse("$year-${month + 1}-$day", DateTimeFormat.forPattern(Constants.FORMAT_YY_MM_dd))
            repository.putDateTime("cachedDate", cachedDate)
            calView!!.refreshEventsFragment()
            calView!!.updateCurrentDate(cachedDate.toString(Constants.FORMAT_DD_MM_YYYY))
        }
    }

    override fun onCreate() {
        calView!!.setDate(repository.getDateTime("cachedDate").millis)
        calView!!.updateCurrentDate(repository.getDateString("cachedDate"))
    }
}