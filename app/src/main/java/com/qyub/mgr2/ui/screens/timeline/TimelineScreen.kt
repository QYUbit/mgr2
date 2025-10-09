package com.qyub.mgr2.ui.screens.timeline

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.ui.components.EventBottomSheet
import com.qyub.mgr2.ui.components.Timeline
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    vm: TimelineViewModel,
    onMenuRequest: () -> Unit,
    startDay: LocalDate = LocalDate.now()
) {
    var showSheet by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<Event?>(null) }

    val scrollState = rememberScrollState()

    val displayDay by vm.displayDay.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    val basePage = Int.MAX_VALUE / 2
    fun pageForDay(day: LocalDate): Int =
        basePage + ChronoUnit.DAYS.between(LocalDate.now(), day).toInt()
    fun dayForPage(page: Int): LocalDate =
        LocalDate.now().plusDays((page - basePage).toLong())

    LaunchedEffect(startDay) {
        vm.setDay(startDay)
        pagerState.scrollToPage(pageForDay(startDay))
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val day = dayForPage(page)
            if (day != displayDay) vm.setDay(day)
        }
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
                title = { Text(displayDay.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))) },
                navigationIcon = {
                    IconButton(
                        onClick = onMenuRequest
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        //Icon(R.drawable.collection)
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                    navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
                ),
            )
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(padding).fillMaxSize(),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )
        ) { page ->
            val day = dayForPage(page)
            val events by vm.eventsForDateFlow(day).collectAsState()

            Timeline(
                events = events,
                onEventClick = { e ->
                    eventToEdit = e
                    showSheet = true
                },
                isCurrentDay = day == LocalDate.now(),
                scrollState = scrollState
            )
        }

        if (showSheet) {
            EventBottomSheet(
                initialEvent = eventToEdit,
                currentDate = displayDay,
                onSave = { input ->
                    if (eventToEdit == null) vm.addEvent(input) else vm.updateEvent(input)
                    showSheet = false
                },
                onDismiss = { showSheet = false },
                onDelete = { vm.deleteEvent(eventToEdit!!) }
            )
        }
    }
}