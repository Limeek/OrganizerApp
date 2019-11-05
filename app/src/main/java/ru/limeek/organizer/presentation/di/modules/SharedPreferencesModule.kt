package ru.limeek.organizer.presentation.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.presentation.di.scopes.AppScope

@Module
open class SharedPreferencesModule(private val appContext: Context) {
    @Provides
    @AppScope
    open fun provideSharedPreferences() : SharedPreferences{
        return appContext.getSharedPreferences("OrganizerSharedPrefs", Context.MODE_PRIVATE)
    }
}