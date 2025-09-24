package com.qyub.mgr2.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "events",
    indices = [Index(value = ["exceptionParentId"])]
)
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val title: String,
    val description: String? = null,

    val isRepeating: Boolean = false,
    val repeatOn: List<Int> = emptyList(),

    val dateEpochDay: Long? = null,

    val startTime: Long? = null,
    val endTime: Long? = null,

    val isException: Boolean = false,
    val exceptionParentId: Long? = null,

    val colorHex: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toNewEvent(): NewEvent {
        return NewEvent(
            title = title,
            description = description,
            isRepeating = isRepeating,
            repeatOn = repeatOn,
            date = dateEpochDay?.let { LocalDate.ofEpochDay(it) },
            startTime = startTime?.let { LocalTime.ofSecondOfDay(it) },
            endTime = endTime?.let { LocalTime.ofSecondOfDay(it) },
            colorHex = colorHex
        )
    }
}

data class NewEvent(
    val title: String,
    val description: String?,
    val isRepeating: Boolean,
    val repeatOn: List<Int>,
    val date: LocalDate?,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val colorHex: String?
)