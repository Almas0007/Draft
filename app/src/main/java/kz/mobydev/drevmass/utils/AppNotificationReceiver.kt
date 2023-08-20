package kz.mobydev.drevmass.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import kz.mobydev.drevmass.R

class AppNotificationReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle the alarm here and show the notification
            val data = intent.getStringExtra("YOUR_EXTRA_DATA_KEY")

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create a notification channel for Android Oreo and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "Your_Channel_Id"
                val channelName = "Your_Channel_Name"
                val channelDescription = "Your_Channel_Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }
                notificationManager.createNotificationChannel(channel)
            }
            val titleNotification = context.getString(R.string.notify_title)
            val subtitleNotification = context.getString(R.string.notify_description)
            // Create the notification
            val notificationBuilder = NotificationCompat.Builder(context, "Your_Channel_Id")
                .setSmallIcon(R.drawable.drevmass_logo)
                .setContentTitle(titleNotification)
                .setContentText(subtitleNotification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // Show the notification
            notificationManager.notify(0, notificationBuilder.build())
        }
}