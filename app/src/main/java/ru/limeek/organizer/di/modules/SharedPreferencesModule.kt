package ru.limeek.organizer.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.scopes.AppScope

@Module
class SharedPreferencesModule(val app: App) {
    @Provides
    @AppScope
    fun provideSharedPreferences() : SharedPreferences{
        return app.applicationContext.getSharedPreferences("OrganizerSharedPrefs", Context.MODE_PRIVATE)
    }
}