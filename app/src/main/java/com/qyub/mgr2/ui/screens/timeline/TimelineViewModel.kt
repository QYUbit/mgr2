package com.qyub.mgr2.ui.screens.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.repo.EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TimelineUiState(
    val displayDay: LocalDate = LocalDate.now(),
    val eventsForDay: List<Event> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
class TimelineViewModel(
    private val repo: EventRepository
) : ViewModel() {

    private val _displayDay = MutableStateFlow(LocalDate.now())
    private val _uiState = MutableStateFlow(TimelineUiState())

    private val eventCache = mutableMapOf<LocalDate, StateFlow<List<Event>>>()

    init {
        viewModelScope.launch {
            _displayDay
                .flatMapLatest { date ->
                    eventsForDateFlow(date).map { events ->
                        TimelineUiState(displayDay = date, eventsForDay = events)
                    }
                }
                .collect { _uiState.value = it }
        }
    }

    fun eventsForDateFlow(date: LocalDate): StateFlow<List<Event>> {
        return eventCache.getOrPut(date) {
            repo.eventsForDate(date)
                .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        }
    }

    fun showNextDay() { _displayDay.value = _displayDay.value.plusDays(1) }
    fun showPreviousDay() { _displayDay.value = _displayDay.value.minusDays(1) }

    fun addEvent(event: Event) {
        viewModelScope.launch { repo.addEvent(event) }
    }

    fun updateEvent(event: Event) {
        // Not working
        viewModelScope.launch { repo.updateEvent(event) }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch { repo.deleteEvent(event) }
    }
}