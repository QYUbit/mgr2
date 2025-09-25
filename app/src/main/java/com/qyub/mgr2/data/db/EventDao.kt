package com.qyub.mgr2.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.qyub.mgr2.data.models.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE date BETWEEN :start AND :end ORDER BY date")
    fun eventsBetween(start: Long, end: Long): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE isRepeating = 0 AND date = :targetDate")
    fun getFixedEventsForDay(targetDate: Long): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE isRepeating = 1 AND repeatOn LIKE :weekDay")
    fun getRepeatingEventsForDay(weekDay: String): Flow<List<Event>>

    @Insert
    suspend fun insert(event: Event)

    @Delete
    suspend fun delete(event: Event)
}