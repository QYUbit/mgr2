package com.qyub.mgr2.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class PopupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra("event_id", -1)
        val eventTitle = intent.getStringExtra("event_title") ?: "Event"
        val eventDescription = intent.getStringExtra("event_description") ?: ""
        val reminderMinutes = intent.getIntExtra("reminder_minutes", 15)

        if (eventId == -1L) return

        val popupIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NO_USER_ACTION
            putExtra("event_id", eventId)
            putExtra("event_title", eventTitle)
            putExtra("event_description", eventDescription)
            putExtra("is_alarm", false)
        }
        context.startActivity(popupIntent)

        createPopupChannel(context)
        showPopupNotification(context, eventId, eventTitle, eventDescription, reminderMinutes)
    }

    private fun createPopupChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Popups"
            val descriptionText = "Popup notifications for events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                enableLights(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showPopupNotification(
        context: Context,
        eventId: Long,
        title: String,
        description: String,
        reminderMinutes: Int
    ) {
        val dismissIntent = Intent(context, AlarmDismissReceiver::class.java).apply {
            putExtra("event_id", eventId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NO_USER_ACTION
            putExtra("event_id", eventId)
            putExtra("event_title", title)
            putExtra("event_description", description)
            putExtra("is_alarm", false)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            eventId.toInt(),
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val timeText = NotificationReceiver.formatTime(reminderMinutes)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle(title)
            .setContentText("$timeText${if (description.isNotEmpty()) " • $description" else ""}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$timeText${if (description.isNotEmpty()) "\n$description" else ""}"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Close", dismissPendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(fullScreenPendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(eventId.toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "event_popups"
    }
}
