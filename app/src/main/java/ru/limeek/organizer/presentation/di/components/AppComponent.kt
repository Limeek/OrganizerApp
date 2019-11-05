package ru.limeek.organizer.presentation.di.components

import dagger.Component
import ru.limeek.organizer.presentation.app.App
import ru.limeek.organizer.presentation.di.modules.*
import ru.limeek.organizer.presentation.di.scopes.AppScope

@AppScope
@Component(modules = [AppModule::class, RoomModule::class, SharedPreferencesModule::class, RetrofitModule::class])
interface AppComponent {
    fun newViewComponent(viewModelModule: ViewModelModule): ViewComponent
    fun inject(app : App)
}