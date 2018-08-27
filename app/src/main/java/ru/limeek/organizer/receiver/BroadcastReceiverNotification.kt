package ru.limeek.organizer.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import org.joda.time.DateTime
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.event.eventdetails.view.EventDetailsActivity
import ru.limeek.organizer.util.Constants

class BroadcastReceiverNotification : BroadcastReceiver() {
    val logTag = "BroadcastReceiverNotification"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.wtf(logTag,"onReceive")
        createEventNotification(context,intent)
    }

    fun createEventNotification(context: Context?, intent: Intent?){

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


        App.instance.notificationManager.notify(eventId.toInt(),notification)

    }

}