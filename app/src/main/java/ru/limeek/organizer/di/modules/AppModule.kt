package ru.limeek.organizer.di.modules

import dagger.Module
import dagger.Provides
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.scopes.AppScope

@Module
class AppModule {
    @Provides
    @AppScope
    fun provideApp() : App{
        return App()
    }
}