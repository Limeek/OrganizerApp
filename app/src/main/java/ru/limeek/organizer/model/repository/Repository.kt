package ru.limeek.organizer.model.repository

import ru.limeek.organizer.api.DarkSkyApi
import ru.limeek.organizer.api.NewsApi
import ru.limeek.organizer.database.AppDatabase
import ru.limeek.organizer.model.OrganizerSharedPreferences

data class Repository(val database: AppDatabase,
                 val sharedPreferences: OrganizerSharedPreferences,
                 val darkSkyApi: DarkSkyApi,
                 val newsApi: NewsApi)