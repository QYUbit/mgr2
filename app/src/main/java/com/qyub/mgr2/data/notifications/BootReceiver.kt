package com.qyub.mgr2.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.qyub.mgr2.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            rescheduleAllNotifications(context)
        }
    }

    private fun rescheduleAllNotifications(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val eventDao = db.eventDao()
                val scheduler = NotificationScheduler(context)

                val today = LocalDate.now()
                val futureDate = today.plusDays(30)

                val events = eventDao.eventsBetween(
                    today.toEpochDay(),
                    futureDate.toEpochDay()
                ).first()

                events.forEach { event ->
                    if (event.hasNotification && event.startTime != null && event.date != null) {
                        scheduler.scheduleNotification(event, event.notificationMinutes)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
