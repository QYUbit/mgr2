package com.qyub.mgr2.data.repo

import com.qyub.mgr2.data.db.EventDao
import com.qyub.mgr2.data.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import java.time.LocalDate

class EventRepository(
    private val dao: EventDao
) {
    fun eventsForDate(date: LocalDate): Flow<List<Event>> {
        val epochDay = date.toEpochDay()
        val dayIndex = (date.dayOfWeek.value - 1) % 7

        return combine(
            dao.getFixedEventsForDay(epochDay),
            dao.getRepeatingEventsForDay("%$dayIndex%")
        ) { fixed, repeating ->
            fixed + repeating
        }
    }

    suspend fun addEvent(event: Event) {
        dao.insert(event)
    }

    suspend fun updateEvent(event: Event) {
        dao.update(event)
    }

    suspend fun deleteEvent(event: Event) = withContext(Dispatchers.IO) {
        dao.delete(event)
    }
}