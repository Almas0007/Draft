package kz.mobydev.drevmass.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

class NotificationManager(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setNotification(time: String, dayOfWeek: Int) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, time.substringBefore(":").toInt())
            set(Calendar.MINUTE, time.substringAfter(":").toInt())
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE or 0)
        } else {
            PendingIntent.getBroadcast(context, 0, intent!!, 0)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun cancelNotification() {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE or 0)
        } else {
            PendingIntent.getBroadcast(context, 0, intent!!, 0)
        }
        alarmManager.cancel(pendingIntent)
    }
}

