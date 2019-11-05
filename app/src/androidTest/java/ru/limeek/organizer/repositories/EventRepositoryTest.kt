package ru.limeek.organizer.repositories

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.limeek.organizer.components.DaggerTestAppComponent
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.event.RemindTime
import ru.limeek.organizer.modules.UnitTestRoomModule
import ru.limeek.organizer.presentation.di.modules.SharedPreferencesModule
import javax.inject.Inject


@RunWith(JUnit4::class)
class EventRepositoryTest {

    @Inject
    lateinit var eventRepo: EventRepository

    val eventToInsert = Event(1, DateTime.now(), "123", RemindTime.FIVEMIN)


    @Before
    fun before(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        DaggerTestAppComponent.builder()
                .unitTestRoomModule(UnitTestRoomModule(context))
                .sharedPreferencesModule(SharedPreferencesModule(context))
                .build()
                .inject(this)
    }

    @Test
    fun testInsert(){
        runBlocking {
            eventRepo.insert(eventToInsert)
            Assert.assertEquals(eventToInsert, eventRepo.getAllEvents()[0])
        }
    }

    @Test
    fun testUpdate(){
        runBlocking {
            eventRepo.insert(eventToInsert)

            val eventToUpdate = eventRepo.getAllEvents()[0].apply {
                this.summary = "321"
            }
            eventRepo.update(eventToUpdate)


            Assert.assertEquals(eventToUpdate, eventRepo.getAllEvents()[0])
        }
    }

    @Test
    fun testDelete(){
        runBlocking {
            eventRepo.insert(eventToInsert)

            val eventToDelete = eventRepo.getAllEvents()[0]

            eventRepo.delete(eventToDelete)

            Assert.assertEquals(true, eventRepo.getAllEvents().isEmpty())
        }
    }


}