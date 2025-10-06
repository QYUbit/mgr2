package com.qyub.mgr2.ui.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.repo.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventsViewModel(
    private val repo: EventRepository
) : ViewModel() {
    private val _events = MutableStateFlow(emptyMap<LocalDate, List<Event>>())
    val events = _events.asStateFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            repo.allEvents().collect { _ ->

            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch { repo.addEvent(event) }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch { repo.updateEvent(event) }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch { repo.deleteEvent(event) }
    }
}