package com.qyub.mgr2.ui.screens.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.repo.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TimelineUiState(
    val displayDay: LocalDate = LocalDate.now(),
    val eventsForDay: List<Event> = emptyList()
)

class TimelineViewModel(
    private val repo: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState = _uiState.asStateFlow()

     init {
         loadDay()
     }

    private fun loadDay () {
         viewModelScope.launch {
             repo.eventsForDate(_uiState.value.displayDay).collect { events ->
                 _uiState.value = _uiState.value.copy(
                     eventsForDay = events
                 )
             }
         }
     }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            repo.addEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            repo.deleteEvent(event)
        }
    }
}