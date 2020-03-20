package ru.limeek.organizer.presentation.di.modules.fragments

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.domain.usecases.DeleteLocationUseCase
import ru.limeek.organizer.domain.usecases.InsertLocationUseCase
import ru.limeek.organizer.domain.usecases.UpdateLocationUseCase
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.viewmodels.LocationDetailsViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.LocationDetailsViewModelFactory
import ru.limeek.organizer.presentation.views.LocationDetailsFragment

@Module
class LocationDetailsFragmentModule {
    @Provides
    @FragmentScope
    fun provideLocationDetailsViewModel(fragment: LocationDetailsFragment,
                                        insertLocationUseCase: InsertLocationUseCase,
                                        updateLocationUseCase: UpdateLocationUseCase,
                                        deleteLocationUseCase: DeleteLocationUseCase): LocationDetailsViewModel {
        return ViewModelProviders.of(fragment, LocationDetailsViewModelFactory(insertLocationUseCase,
                updateLocationUseCase,
                deleteLocationUseCase)).get(LocationDetailsViewModel::class.java)
    }
}