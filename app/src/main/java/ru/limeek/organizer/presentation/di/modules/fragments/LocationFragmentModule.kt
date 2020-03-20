package ru.limeek.organizer.presentation.di.modules.fragments

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.domain.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.viewmodels.LocationViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.LocationViewModelFactory
import ru.limeek.organizer.presentation.views.LocationFragment

@Module
class LocationFragmentModule {
    @Provides
    @FragmentScope
    fun provideLocationViewModel(fragment: LocationFragment,
                                 getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase): LocationViewModel{
        return ViewModelProviders
                .of(fragment, LocationViewModelFactory(getUserCreatedLocationsUseCase))
                .get(LocationViewModel::class.java)
    }
}