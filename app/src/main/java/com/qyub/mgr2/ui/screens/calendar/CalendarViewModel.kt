package com.qyub.mgr2.ui.screens.calendar

import androidx.lifecycle.ViewModel
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.repo.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.YearMonth

data class CalendarUiState(
    val displayMonth: YearMonth = YearMonth.now(),
    val eventsForMonth: Map<Long, List<Event>> = emptyMap()
)

class CalendarViewModel(
    private val repo: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

   /* init {
        loadMonth()
    }

    fun nextMonth() {
        _uiState.value = _uiState.value.copy(displayMonth = _uiState.value.displayMonth.plusMonths(1))
        loadMonth()
    }

    private fun loadMonth() {
        viewModelScope.launch {
            repo.eventsForMonth(_uiState.value.displayMonth).collect { events ->
                _uiState.value = _uiState.value.copy(
                    eventsForMonth = events.groupBy { it.dateEpochDay }
                )
            }
        }
    }*/

}