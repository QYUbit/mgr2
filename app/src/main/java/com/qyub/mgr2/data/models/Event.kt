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

    val date: LocalDate? = null,

    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,

    val isException: Boolean = false,
    val exceptionParentId: Long? = null,

    val colorHex: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)