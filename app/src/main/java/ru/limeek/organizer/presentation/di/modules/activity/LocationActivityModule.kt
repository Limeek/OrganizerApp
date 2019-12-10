package ru.limeek.organizer.presentation.di.modules.activity

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.domain.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.viewmodels.LocationViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.LocationViewModelFactory
import ru.limeek.organizer.presentation.views.LocationActivity

@Module
class LocationActivityModule {
    @Provides
    @ActivityScope
    fun provideLocationViewModel(activity: LocationActivity,
                                 getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase): LocationViewModel{
        return ViewModelProviders
                .of(activity, LocationViewModelFactory(getUserCreatedLocationsUseCase))
                .get(LocationViewModel::class.java)
    }
}