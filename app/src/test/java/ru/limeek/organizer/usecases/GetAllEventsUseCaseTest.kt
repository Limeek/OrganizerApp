package ru.limeek.organizer.usecases


import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.event.RemindTime
import ru.limeek.organizer.domain.usecases.GetAllEventsUseCase

@RunWith(JUnit4::class)
class GetAllEventsUseCaseTest{

    private lateinit var useCase: GetAllEventsUseCase
    @Mock
    private lateinit var eventRepo: EventRepository

    @Before
    fun setup(){
        eventRepo = Mockito.mock(EventRepository::class.java)
        useCase = GetAllEventsUseCase(eventRepo)
    }

    @Test
    fun returnEvents(){
        runBlocking {
            val eventsToReturn = listOf(Event(DateTime.now(), "123", RemindTime.FIVEMIN))
            Mockito.`when`(eventRepo.getAllEvents()).thenReturn(eventsToReturn)
            val events = useCase.execute()
            Assert.assertEquals(events, eventsToReturn)
        }
    }
}