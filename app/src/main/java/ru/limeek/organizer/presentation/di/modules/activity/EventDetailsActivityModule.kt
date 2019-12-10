package ru.limeek.organizer.presentation.di.modules.activity

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.domain.usecases.DeleteEventUseCase
import ru.limeek.organizer.domain.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.domain.usecases.InsertEventUseCase
import ru.limeek.organizer.domain.usecases.UpdateEventUseCase
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.viewmodels.EventDetailsViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.EventDetailsViewModelFactory
import ru.limeek.organizer.presentation.views.EventDetailsActivity

@Module
class EventDetailsActivityModule {

    @Provides
    @ActivityScope
    fun provideEventDetailsViewModel(activity: EventDetailsActivity,
                                     sharedPrefsRepo: SharedPrefsRepository,
                                     insertEventUseCase: InsertEventUseCase,
                                     updateEventUseCase: UpdateEventUseCase,
                                     deleteEventUseCase: DeleteEventUseCase,
                                     getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase
    ): EventDetailsViewModel {
        return ViewModelProviders.of(activity, EventDetailsViewModelFactory(sharedPrefsRepo,
                insertEventUseCase,
                updateEventUseCase,
                deleteEventUseCase,
                getUserCreatedLocationsUseCase))
                .get(EventDetailsViewModel::class.java)
    }
}