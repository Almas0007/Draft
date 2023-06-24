package kz.mobydev.drevmass.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kz.mobydev.drevmass.MainActivity

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appIntent = Intent(context, MainActivity::class.java)
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appIntent.putExtra("targetFragment", "SplashFragment")

        context.startActivity(appIntent)
    }
}
