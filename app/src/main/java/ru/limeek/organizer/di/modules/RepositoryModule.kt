package ru.limeek.organizer.di.modules

import dagger.Module
import dagger.Provides
import ru.limeek.organizer.api.DarkSkyApi
import ru.limeek.organizer.api.NewsApi
import ru.limeek.organizer.database.AppDatabase
import ru.limeek.organizer.di.scopes.PresenterScope
import ru.limeek.organizer.model.OrganizerSharedPreferences
import ru.limeek.organizer.model.repository.Repository

@Module
class RepositoryModule{
    @Provides
    @PresenterScope
    fun provideRepository(appDatabase: AppDatabase, newsApi: NewsApi, darkSkyApi: DarkSkyApi, sharedPreferences: OrganizerSharedPreferences) : Repository{
        return Repository(appDatabase,sharedPreferences,darkSkyApi,newsApi)
    }
}