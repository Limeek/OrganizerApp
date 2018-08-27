package ru.limeek.organizer.di.components

import dagger.Component
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.*
import ru.limeek.organizer.di.scopes.AppScope

@AppScope
@Component(modules = [AppModule::class, RoomModule::class, SharedPreferencesModule::class, RetrofitModule::class])
interface AppComponent {
    fun newViewComponent(presenterModule: PresenterModule) : ViewComponent
    fun newPresenterComponent(repositoryModule: RepositoryModule) : PresenterComponent

    fun inject(app : App)
}