package ru.limeek.organizer.app

import android.Manifest
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import com.squareup.leakcanary.LeakCanary
import ru.limeek.organizer.R
import ru.limeek.organizer.database.AppDatabase
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
    lateinit var database: AppDatabase

    lateinit var component: AppComponent

    lateinit var notificationManager: NotificationManager
    lateinit var locationManager: LocationManager
    lateinit var alarmManager : AlarmManager

    override fun onCreate() {
        super.onCreate()
        if(LeakCanary.isInAnalyzerProcess(this)){
            return
        }
        LeakCanary.install(this)
        component = DaggerAppComponent.builder()
                .roomModule(RoomModule(this))
                .sharedPreferencesModule(SharedPreferencesModule(this))
                .retrofitModule(RetrofitModule())
                .build()

        component.inject(this)

        instance = this

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL) == null)
            notificationManager.createNotificationChannel(NotificationChannel(Constants.NOTIFICATION_CHANNEL,resources.getString(R.string.app_name),NotificationManager.IMPORTANCE_DEFAULT))

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    fun checkLocationPermission() : Boolean{
        return (baseContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                baseContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    fun deviceIsOffline() : Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo == null || !netInfo.isConnected
    }

}
