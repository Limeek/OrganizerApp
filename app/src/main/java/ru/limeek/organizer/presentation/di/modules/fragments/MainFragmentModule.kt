package ru.limeek.organizer.presentation.di.modules.fragments

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.viewmodels.MainViewModel
import ru.limeek.organizer.presentation.views.MainFragment

@Module
class MainFragmentModule {
    @Provides
    @FragmentScope
    fun provideMainViewModel(fragment: MainFragment): MainViewModel {
        return ViewModelProviders.of(fragment).get(MainViewModel::class.java)
    }
}