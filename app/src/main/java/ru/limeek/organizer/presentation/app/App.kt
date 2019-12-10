package ru.limeek.organizer.presentation.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.limeek.organizer.presentation.di.components.DaggerAppComponent

class App : DaggerApplication() {

    lateinit var component: AndroidInjector<App>

    override fun onCreate() {
        component = DaggerAppComponent.
                factory()
                .create(this)
        
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return component
    }
}
