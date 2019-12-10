package ru.limeek.organizer.presentation.di.components

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.di.modules.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RoomModule::class,
    SharedPreferencesModule::class,
    RetrofitModule::class,
    BuilderModule::class])
interface AppComponent: AndroidInjector<App>{
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>{
        abstract override fun create(@BindsInstance app: App): AppComponent
    }
}