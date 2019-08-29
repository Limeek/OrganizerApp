package ru.limeek.organizer.app

import android.app.Application
import android.content.SharedPreferences
import org.joda.time.DateTime
import ru.limeek.organizer.di.components.AppComponent
import ru.limeek.organizer.di.components.DaggerAppComponent
import ru.limeek.organizer.di.modules.RetrofitModule
import ru.limeek.organizer.di.modules.RoomModule
import ru.limeek.organizer.di.modules.SharedPreferencesModule
import ru.limeek.organizer.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class App : Application() {
    companion object {
        lateinit var instance : App
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
//        if(LeakCanary.isInAnalyzerProcess(this)){
//            return
//        }
//        LeakCanary.install(this)
        component = DaggerAppComponent.builder()
                .roomModule(RoomModule(this))
                .sharedPreferencesModule(SharedPreferencesModule(this))
                .retrofitModule(RetrofitModule())
                .build()

        component.inject(this)

        instance = this
        sharedPreferences.edit().putString(Constants.CACHED_DATE, DateTime.now().toString(Constants.FORMAT_DD_MM_YY_HH_MM)).apply()
    }
}
