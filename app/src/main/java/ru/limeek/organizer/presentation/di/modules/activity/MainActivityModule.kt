package ru.limeek.organizer.presentation.di.modules.activity

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.viewmodels.MainViewModel
import ru.limeek.organizer.presentation.views.MainActivity

@Module
class MainActivityModule {
    @Provides
    @ActivityScope
    fun provideMainViewModel(activity: MainActivity): MainViewModel {
        return ViewModelProviders.of(activity).get(MainViewModel::class.java)
    }
}