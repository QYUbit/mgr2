package com.qyub.mgr2.data.repo

import com.qyub.mgr2.data.db.EventDao
import com.qyub.mgr2.data.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth

class EventRepository(
    private val dao: EventDao
) {
    fun eventsForMonth(month: YearMonth): Flow<List<Event>> {
        val start = month.atDay(1).toEpochDay()
        val end = month.atEndOfMonth().toEpochDay()
        return dao.eventsBetween(start, end).flowOn(Dispatchers.IO)
    }

    fun eventsForDate(date: LocalDate): Flow<List<Event>> {
        val epochDay = date.toEpochDay()
        val dayIndex = (date.dayOfWeek.value - 1) % 7

        println("%$dayIndex%")

        return combine(
            dao.getFixedEventsForDay(epochDay),
            dao.getRepeatingEventsForDay("%$dayIndex%")
        ) { fixed, repeating ->
            println(repeating)
            fixed + repeating
        }
    }

    suspend fun addEvent(event: Event) {
        dao.insert(event)
    }

    suspend fun deleteEvent(event: Event) = withContext(Dispatchers.IO) {
        dao.delete(event)
    }
}