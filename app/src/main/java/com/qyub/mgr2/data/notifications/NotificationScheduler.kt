package com.qyub.mgr2.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.models.NotificationType
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(event: Event, reminderMinutes: Int = 15) {
        if (event.startTime == null || event.date == null) return

        val eventDateTime = LocalDateTime.of(event.date, event.startTime)
        val notificationTime = eventDateTime.minusMinutes(reminderMinutes.toLong())

        if (notificationTime.isBefore(LocalDateTime.now())) return

        val triggerTime = notificationTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val receiverClass = when (event.notificationType) {
            NotificationType.REMINDER -> NotificationReceiver::class.java
            NotificationType.ALARM -> AlarmReceiver::class.java
            NotificationType.POPUP -> PopupReceiver::class.java
        }

        val intent = Intent(context, receiverClass).apply {
            putExtra("event_id", event.id)
            putExtra("event_title", event.title)
            putExtra("event_description", event.description ?: "")
            putExtra("reminder_minutes", reminderMinutes)
            putExtra("notification_type", event.notificationType.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            generateRequestCode(event.id, reminderMinutes),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancelNotification(eventId: Long, reminderMinutes: Int = 15) {
        listOf(
            NotificationReceiver::class.java,
            AlarmReceiver::class.java,
            PopupReceiver::class.java
        ).forEach { receiverClass ->
            val intent = Intent(context, receiverClass)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                generateRequestCode(eventId, reminderMinutes),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            pendingIntent?.let {
                alarmManager.cancel(it)
                it.cancel()
            }
        }
    }

    fun rescheduleNotification(event: Event, reminderMinutes: Int = 15) {
        cancelNotification(event.id, reminderMinutes)
        scheduleNotification(event, reminderMinutes)
    }

    private fun generateRequestCode(eventId: Long, reminderMinutes: Int): Int {
        return (eventId.toString() + reminderMinutes.toString()).hashCode()
    }
}
