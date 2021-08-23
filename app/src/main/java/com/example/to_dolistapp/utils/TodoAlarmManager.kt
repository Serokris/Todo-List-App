package com.example.to_dolistapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.to_dolistapp.R
import com.example.to_dolistapp.utils.notifications.NotificationReceiver
import java.util.*

class TodoAlarmManager {
    companion object {
        fun createAlarm(context: Context, calendar : Calendar) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (calendar.before(Calendar.getInstance()))  {
                calendar.add(Calendar.DATE, 1)
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

        fun cancelReminder(context: Context) {
            if (isAlarmSet(context)) {
                val intent = Intent(context, NotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()

                Toast.makeText(context, R.string.reminder_canceled, Toast.LENGTH_SHORT).show()
                return
            }
            Toast.makeText(context, R.string.you_dont_have_reminder, Toast.LENGTH_SHORT).show()
        }

        fun cancelPendingIntent(context: Context) {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            pendingIntent.cancel()
        }

        private fun isAlarmSet(context: Context): Boolean {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_NO_CREATE)
            return pendingIntent != null
        }
    }
}