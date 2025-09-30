package com.qyub.mgr2.ui.screens.calendar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.qyub.mgr2.ui.components.Calendar
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onMenuRequest: () -> Unit,
    onDateClick: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    val basePage = Int.MAX_VALUE / 2
    fun monthForPage(page: Int): YearMonth =
        YearMonth.now().plusMonths((page - basePage).toLong())

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val month = monthForPage(page)
            if (month != currentMonth) currentMonth = month
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(currentMonth.toString())
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
            val month = monthForPage(page)

            Calendar(
                month = month,
                onDayClick = { day ->
                    onDateClick(LocalDate.of(currentMonth.year, currentMonth.month, day))
                },
                isCurrentMonth = month == YearMonth.now()
            )
        }
    }
}
