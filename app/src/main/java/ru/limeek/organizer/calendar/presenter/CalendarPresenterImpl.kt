package ru.limeek.organizer.calendar.presenter

import android.widget.CalendarView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.app.App
import ru.limeek.organizer.calendar.view.AppCalendarView
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.model.repository.Repository
import ru.limeek.organizer.util.Constants
import javax.inject.Inject


class CalendarPresenterImpl (var calView : AppCalendarView?) : CalendarPresenter {
    private val logTag = "CalendarPresenterImpl"

    @Inject
    lateinit var repository: Repository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun onDateChange() : CalendarView.OnDateChangeListener{
        return CalendarView.OnDateChangeListener { _: CalendarView, year: Int, month: Int, day: Int ->
            val cachedDate = DateTime.parse("$year-${month+1}-$day", DateTimeFormat.forPattern(Constants.FORMAT_YY_MM_dd))
            repository.sharedPreferences.putDateTime("cachedDate",cachedDate)
            calView!!.refreshEventsFragment()
            calView!!.currentDate.text = cachedDate.toString(Constants.FORMAT_DD_MM_YYYY)
        }
    }

    override fun onCreate() {
        repository.sharedPreferences.putDateTime("cachedDate",DateTime.now())
        calView!!.currentDate.text = repository.sharedPreferences.getDateString("cachedDate")
    }
}