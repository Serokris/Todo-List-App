package com.example.to_dolistapp.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.to_dolistapp.utils.Alarm

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationTodo.createNotification(context)
        Alarm.cancelPendingIntent(context)
    }
}