package ru.limeek.organizer.presenters

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.app.App
import ru.limeek.organizer.views.AppCalendarView
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.util.Constants
import javax.inject.Inject


class CalendarPresenterImpl(var calView: AppCalendarView?) : CalendarPresenter {
    private val logTag = "CalendarPresenterImpl"

    @Inject
    lateinit var repository: SharedPrefsRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun onDateChange(day: Int, month: Int, year: Int) {
        val cachedDate = DateTime.parse("$year-${month + 1}-$day", DateTimeFormat.forPattern(Constants.FORMAT_YY_MM_dd))
        repository.putDateTime("cachedDate", cachedDate)
    }

    override fun onCreate() {
        calView!!.setDate(repository.getDateTime("cachedDate").millis)
    }
}