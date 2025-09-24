package com.qyub.mgr2.ui.screens.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qyub.mgr2.data.models.NewEvent
import com.qyub.mgr2.ui.components.EventBottomSheet
import com.qyub.mgr2.ui.components.EventCard

@Composable
fun TimelineScreen(vm: TimelineViewModel) {
    val state by vm.uiState.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<NewEvent?>(null) }

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
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
            ) {
                Text("+", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = state.eventsForDay) { event ->
                    Card(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth()
                            .clickable {
                                eventToEdit = event.toNewEvent()
                                showSheet = true
                            }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(event.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
                            if (!event.description.isNullOrEmpty()) {
                                Text(event.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                            }
                            Text("${event.dateEpochDay} ${event.startTime ?: "??"} - ${event.endTime ?: "??"}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }


            if (showSheet) {
                EventBottomSheet(
                    initialEvent = eventToEdit,
                    onSave = { input ->
                        if (eventToEdit == null) {
                            vm.addEvent(input)
                        } else {
                            //vm.updateEvent(input)
                            TODO("Not implemented yet")
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
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.eventsForDay.forEach { event ->
            EventCard(event)
        }
    }
}