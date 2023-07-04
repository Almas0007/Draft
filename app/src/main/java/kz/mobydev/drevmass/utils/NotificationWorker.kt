package kz.mobydev.drevmass.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.WorkerParameters
import kz.mobydev.drevmass.R

import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import java.util.Calendar

import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.MainActivity

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    private val appComponents by lazy { App.appComponents }
    override suspend fun doWork(): Result {
        val dayRequest = inputData.getString("dayRequest")
        val time = inputData.getString("time")

        // Проверяем текущий день недели
        val currentDayOfWeek = getCurrentDayOfWeek()

        // Проверяем, нужно ли отправлять уведомление в этот день
        if (shouldSendNotification(currentDayOfWeek, dayRequest)) {
            // Создаем уведомление

            sendNotification(0)
        }

        return Result.success()
    }

    private fun getCurrentDayOfWeek(): Int {
        // Получаем текущий день недели (1 - понедельник, 2 - вторник, и т.д.)
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun shouldSendNotification(currentDayOfWeek: Int, dayRequest: String?): Boolean {
        // Проверяем, нужно ли отправлять уведомление в этот день
        when (currentDayOfWeek) {
            Calendar.MONDAY -> return dayRequest?.get(0) == '1'
            Calendar.TUESDAY -> return dayRequest?.get(1) == '1'
            Calendar.WEDNESDAY -> return dayRequest?.get(2) == '1'
            Calendar.THURSDAY -> return dayRequest?.get(3) == '1'
            Calendar.FRIDAY -> return dayRequest?.get(4) == '1'
            Calendar.SATURDAY -> return dayRequest?.get(5) == '1'
            Calendar.SUNDAY -> return dayRequest?.get(6) == '1'
        }
        return false
    }

    private fun createNotification(): NotificationCompat.Builder {
        // Создаем уведомление
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.drevmass_logo)
            .setContentTitle("Уведомление")
            .setContentText("Пример уведомления")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = applicationContext.getString(R.string.app_name)
        val subtitleNotification = applicationContext.getString(R.string.app_name)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }
}



