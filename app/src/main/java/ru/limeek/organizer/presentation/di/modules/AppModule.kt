package ru.limeek.organizer.presentation.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.presentation.di.scopes.AppScope
import javax.inject.Singleton

@Module
open class AppModule(internal val context: Context) {
    @Provides
    @AppScope
    open fun provideAppContext(): Context {
        return context
    }
}