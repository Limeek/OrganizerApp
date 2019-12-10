package ru.limeek.organizer.presentation.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.presentation.app.App
import javax.inject.Singleton

@Module
open class SharedPreferencesModule {
    @Provides
    @Singleton
    open fun provideSharedPreferences(app: App) : SharedPreferences{
        return app.applicationContext.getSharedPreferences("OrganizerSharedPrefs", Context.MODE_PRIVATE)
    }
}