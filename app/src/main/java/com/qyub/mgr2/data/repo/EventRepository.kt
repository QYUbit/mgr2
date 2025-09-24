package com.qyub.mgr2.data.repo

import com.qyub.mgr2.data.db.Event
import com.qyub.mgr2.data.db.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    suspend fun addEvent(title: String, date: LocalDate) = withContext(Dispatchers.IO) {
        dao.insert(Event(title = title, dateEpochDay = date.toEpochDay()))
    }

    suspend fun deleteEvent(event: Event) = withContext(Dispatchers.IO) {
        dao.delete(event)
    }
}