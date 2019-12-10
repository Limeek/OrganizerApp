package ru.limeek.organizer.components

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import ru.limeek.organizer.data.daos.EventDao
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.fragments.TestEventFragment
import ru.limeek.organizer.modules.UnitTestRoomModule
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.di.modules.BuilderModule
import ru.limeek.organizer.presentation.di.modules.RetrofitModule
import ru.limeek.organizer.presentation.di.modules.SharedPreferencesModule
import ru.limeek.organizer.repositories.EventRepositoryTest
import javax.inject.Singleton

@Component(modules = [UnitTestRoomModule::class,
    SharedPreferencesModule::class,
    RetrofitModule::class,
    BuilderModule::class])
@Singleton
interface TestAppComponent: AndroidInjector<App> {
    fun eventDao(): EventDao
    fun sharedPrefs(): SharedPrefsRepository

    fun inject(eventRepositoryTest: EventRepositoryTest)
    fun inject(testEventFragment: TestEventFragment)

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>{
        abstract override fun create(@BindsInstance app: App): TestAppComponent
    }
}

fun App.setTestComponent(component: AndroidInjector<App>){
    this.component = component.also{
        it.inject(this)
    }
}