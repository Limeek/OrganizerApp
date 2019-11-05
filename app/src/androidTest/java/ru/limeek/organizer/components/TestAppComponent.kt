package ru.limeek.organizer.components

import dagger.Component
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.usecases.GetEventsByCachedDateUseCase
import ru.limeek.organizer.fragments.TestEventFragment
import ru.limeek.organizer.modules.UnitTestRoomModule
import ru.limeek.organizer.presentation.di.components.AppComponent
import ru.limeek.organizer.presentation.di.modules.RetrofitModule
import ru.limeek.organizer.presentation.di.modules.SharedPreferencesModule
import ru.limeek.organizer.presentation.di.scopes.AppScope
import ru.limeek.organizer.repositories.EventRepositoryTest

@Component(modules = [UnitTestRoomModule::class, SharedPreferencesModule::class, RetrofitModule::class])
@AppScope
interface TestAppComponent: AppComponent {
    fun getEventsByCachedDateUseCase(): GetEventsByCachedDateUseCase

    fun inject(testEventFragment: TestEventFragment)
    fun inject(eventRepositoryTest: EventRepositoryTest)
}