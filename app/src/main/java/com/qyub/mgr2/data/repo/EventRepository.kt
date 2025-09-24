package com.qyub.mgr2.data.repo

import com.qyub.mgr2.data.db.EventDao
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.models.NewEvent
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

        return combine(
            dao.getFixedEventsForDay(epochDay),
            dao.getRepeatingEventsForDay(dayIndex.toString())
        ) { fixed, repeating ->
            fixed + repeating
        }
    }

    suspend fun addEvent(input: NewEvent) {
        val event = Event(
            title = input.title,
            description = input.description,
            isRepeating = input.isRepeating,
            repeatOn = input.repeatOn,
            dateEpochDay = input.date?.toEpochDay(),
            startTime = input.startTime?.toSecondOfDay()?.toLong(),
            endTime = input.endTime?.toSecondOfDay()?.toLong(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insert(event)
    }

    suspend fun deleteEvent(input: NewEvent) = withContext(Dispatchers.IO) {
        val event = Event(
            title = input.title,
            description = input.description,
            isRepeating = input.isRepeating,
            repeatOn = input.repeatOn,
            dateEpochDay = input.date?.toEpochDay(),
            startTime = input.startTime?.toSecondOfDay()?.toLong(),
            endTime = input.endTime?.toSecondOfDay()?.toLong(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.delete(event)
    }
}