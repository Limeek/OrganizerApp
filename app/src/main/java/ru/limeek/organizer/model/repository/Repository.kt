package ru.limeek.organizer.model.repository

import org.joda.time.DateTime
import ru.limeek.organizer.api.DarkSkyApi
import ru.limeek.organizer.api.NewsApi
import ru.limeek.organizer.database.AppDatabase
import ru.limeek.organizer.model.OrganizerSharedPreferences

class Repository(){
    lateinit var database: AppDatabase
    lateinit var sharedPreferences: OrganizerSharedPreferences
    lateinit var darkSkyApi: DarkSkyApi
    lateinit var newsApi: NewsApi
    constructor(database: AppDatabase, sharedPreferences: OrganizerSharedPreferences, darkSkyApi: DarkSkyApi, newsApi: NewsApi) : this(){
        this.database = database
        this.sharedPreferences = sharedPreferences
        this.darkSkyApi = darkSkyApi
        this.newsApi = newsApi
        sharedPreferences.putDateTime("cachedDate", DateTime.now())
    }
}