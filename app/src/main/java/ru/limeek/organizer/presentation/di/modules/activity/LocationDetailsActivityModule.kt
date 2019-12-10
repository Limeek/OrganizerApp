package ru.limeek.organizer.presentation.di.modules.activity

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.domain.usecases.DeleteLocationUseCase
import ru.limeek.organizer.domain.usecases.InsertLocationUseCase
import ru.limeek.organizer.domain.usecases.UpdateLocationUseCase
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.viewmodels.LocationDetailsViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.LocationDetailsViewModelFactory
import ru.limeek.organizer.presentation.views.LocationDetailsActivity

@Module
class LocationDetailsActivityModule {
    @Provides
    @ActivityScope
    fun provideLocationDetailsViewModel(activity: LocationDetailsActivity,
                                        insertLocationUseCase: InsertLocationUseCase,
                                        updateLocationUseCase: UpdateLocationUseCase,
                                        deleteLocationUseCase: DeleteLocationUseCase): LocationDetailsViewModel {
        return ViewModelProviders.of(activity, LocationDetailsViewModelFactory(insertLocationUseCase,
                updateLocationUseCase,
                deleteLocationUseCase)).get(LocationDetailsViewModel::class.java)
    }
}