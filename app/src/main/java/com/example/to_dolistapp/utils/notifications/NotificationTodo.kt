package com.example.to_dolistapp.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.to_dolistapp.R
import com.example.to_dolistapp.ui.MainActivity

class NotificationTodo {

    companion object {
        private lateinit var notificationManager: NotificationManager
        private const val CHANNEL_ID = "3550"
        private const val CHANNEL_NAME = "NotificationChannel"
        const val NOTIFICATION_ID = 1

        fun createNotification(context: Context) {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK)

            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_check)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle("You have uncompleted to-do's!")
                .setContentText("Go to the app to see your to-do list")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            createChannelIfNeeded(notificationManager)
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }

        private fun createChannelIfNeeded(notificationManager: NotificationManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }
}