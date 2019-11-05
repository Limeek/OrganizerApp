package ru.limeek.organizer.presentation.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import org.joda.time.DateTime
import ru.limeek.organizer.R
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.views.EventDetailsActivity

class BroadcastReceiverNotification : BroadcastReceiver() {
    val logTag = "BroadcastReceiverNotification"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.wtf(logTag,"onReceive")
        createEventNotification(context,intent)
    }

    private fun createEventNotification(context: Context?, intent: Intent?){
        val eventId = intent?.getLongExtra("eventId",0)
        val notificationTitle = context?.getString(R.string.app_name)
        val notificationContent = intent?.getStringExtra("content")
        val notificationWhen = intent?.getLongExtra("when", DateTime.now().millis)

        val newIntent = Intent(context, EventDetailsActivity::class.java)
        newIntent.putExtra("eventId",eventId)
        newIntent.putExtra("uneditable", true)

        val pendingIntent = PendingIntent.getActivity(context,eventId!!.toInt(),newIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context!!, Constants.NOTIFICATION_CHANNEL)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                .setContentIntent(pendingIntent)
                .setWhen(notificationWhen!!)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(eventId.toInt(),notification)
    }

}