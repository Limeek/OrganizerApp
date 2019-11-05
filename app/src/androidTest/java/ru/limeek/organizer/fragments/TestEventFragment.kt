package ru.limeek.organizer.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import ru.limeek.organizer.R
import ru.limeek.organizer.components.DaggerTestAppComponent
import ru.limeek.organizer.components.TestAppComponent
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.event.RemindTime
import ru.limeek.organizer.modules.UnitTestRoomModule
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.di.modules.SharedPreferencesModule
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.views.EventsFragment
import javax.inject.Inject

class TestEventFragment {

    @Inject
    internal lateinit var eventRepo: EventRepository

    private val component: TestAppComponent by lazy {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App
        val component = DaggerTestAppComponent.builder()
                .unitTestRoomModule(UnitTestRoomModule(app))
                .sharedPreferencesModule(SharedPreferencesModule(app))
                .build()
        app.component = component
        component
    }

    @Before
    fun setup(){
        component.inject(this)

    }

    //Runs because app onCreate is called (date is written there)
    @Test
    fun testDateEquals(){
        val scenario = launchFragmentInContainer<EventsFragment>(null, R.style.Theme_AppCompat)
        val textOfCurrentDate = DateTime.now().toString(Constants.FORMAT_DD_MM_YYYY)
        onView(withId(R.id.tvCurrentDate)).check(matches(withText(textOfCurrentDate)))
    }


    @Test
    fun testRecyclerEventsShowing(){
        runBlocking {
            prepareEvents()
            val scenario = launchFragmentInContainer<EventsFragment>(null, R.style.Theme_AppCompat)
            delay(3000)
            onView(withId(R.id.recViewEvents)).check(matches(hasChildCount(5)))
        }
    }

    private suspend fun prepareEvents(){
        for (i in 1L..5L) {
            eventRepo.insert(Event(i, DateTime.now(), "123", RemindTime.FIVEMIN))
        }
    }
}