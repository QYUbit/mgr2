package com.qyub.mgr2.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.qyub.mgr2.MainActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra("event_id", -1)
        val eventTitle = intent.getStringExtra("event_title") ?: "Event"
        val eventDescription = intent.getStringExtra("event_description") ?: ""
        val reminderMinutes = intent.getIntExtra("reminder_minutes", 15)

        if (eventId == -1L) return

        createNotificationChannel(context)
        showNotification(context, eventId, eventTitle, eventDescription, reminderMinutes)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Reminders"
            val descriptionText = "Notifications for upcoming events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(
        context: Context,
        eventId: Long,
        title: String,
        description: String,
        reminderMinutes: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val timeText = when {
            reminderMinutes == 0 -> "now"
            reminderMinutes < 60 -> "in $reminderMinutes min"
            reminderMinutes == 60 -> "in 1 hour"
            else -> "in ${reminderMinutes / 60} hours"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText("$timeText${if (description.isNotEmpty()) " • $description" else ""}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$timeText${if (description.isNotEmpty()) "\n$description" else ""}"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(eventId.toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "event_reminders"
    }
}