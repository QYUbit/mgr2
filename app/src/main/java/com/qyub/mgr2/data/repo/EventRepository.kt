package com.qyub.mgr2.data.repo

import android.content.Context
import com.qyub.mgr2.data.db.EventDao
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.models.isActiveAtDate
import com.qyub.mgr2.data.notifications.NotificationScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate


class EventRepository(
    private val dao: EventDao,
    private val context: Context? = null
) {
    private val notificationScheduler by lazy {
        context?.let { NotificationScheduler(it) }
    }

    fun allEvents(): Flow<List<Event>> {
        return dao.allEvents()
    }

    fun eventsForDate(date: LocalDate): Flow<List<Event>> {
        return dao
            .getEventsForDate(date.toEpochDay())
            .map { events ->
                events.filter { it.isActiveAtDate(date) }
            }
    }

    suspend fun addEvent(event: Event) {
        dao.insert(event)

        if (event.hasNotification && notificationScheduler != null) {
            notificationScheduler!!.scheduleNotification(event, event.notificationMinutes)
        }
    }

    suspend fun updateEvent(event: Event) {
        dao.update(event)

        notificationScheduler?.let { scheduler ->
            if (event.hasNotification) {
                scheduler.rescheduleNotification(event, event.notificationMinutes)
            } else {
                scheduler.cancelNotification(event.id, event.notificationMinutes)
            }
        }
    }

    suspend fun deleteEvent(event: Event) = withContext(Dispatchers.IO) {
        dao.delete(event)

        notificationScheduler?.cancelNotification(event.id, event.notificationMinutes)
    }

    suspend fun deleteAllEvents() = withContext(Dispatchers.IO) {
        dao.deleteAll()
        // cancel notifications
    }
}
