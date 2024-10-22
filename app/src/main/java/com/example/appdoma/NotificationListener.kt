package com.example.appdoma

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService(){
    companion object{
        var notificationsText: String = ""
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val notificationText = it.notification.extras.getString("android.text")
            val notificationTitle = it.notification.extras.getString("android.title")

            if (notificationText != null && notificationTitle != null){
                notificationsText += "$notificationTitle: $notificationText\n"
                Log.d("NotificationListener", "Nova notificação: $notificationTitle - $notificationText")
            }
        }
    }
}