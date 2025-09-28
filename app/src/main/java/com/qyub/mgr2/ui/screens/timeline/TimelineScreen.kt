package com.qyub.mgr2.ui.screens.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.ui.components.EventBottomSheet
import com.qyub.mgr2.ui.components.Timeline
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    vm: TimelineViewModel,
    onMenuRequest: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<Event?>(null) }

    state.eventsForDay.forEach { event ->
        println("Event ${event.title}: $event")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    eventToEdit = null
                    showSheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Text("+", fontSize = 30.sp, fontWeight = FontWeight.Light)
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(state.displayDay.toString())
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                    navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onMenuRequest
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            ) 
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            Timeline(
                events = state.eventsForDay,
                onEventClick = { event ->
                    eventToEdit = event
                    showSheet = true
                },
                isCurrentDay = state.displayDay == LocalDate.now()
            )

            if (showSheet) {
                EventBottomSheet(
                    initialEvent = eventToEdit,
                    onSave = { input ->
                        if (eventToEdit == null) {
                            vm.addEvent(input)
                        } else {
                            vm.updateEvent(input)
                        }
                        showSheet = false
                    },
                    onDismiss = { showSheet = false },
                    onDelete = {
                        vm.deleteEvent(eventToEdit!!)
                    }
                )
            }
        }

    }
}