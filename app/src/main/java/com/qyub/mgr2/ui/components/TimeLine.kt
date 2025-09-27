package com.qyub.mgr2.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.ui.theme.EventColorCrimson
import com.qyub.mgr2.ui.theme.EventColorLavender
import com.qyub.mgr2.ui.theme.EventColorMint
import com.qyub.mgr2.ui.theme.EventColorSky
import com.qyub.mgr2.ui.theme.EventColorSun
import java.time.LocalTime
import kotlin.math.max

data class UIEvent(
    val event: Event,
    val top: Int,
    val height: Int,
)

private val sampleEvents = listOf(
    Event(
        id = 0,
        title = "Example",
        description = "go shopping",
        startTime = LocalTime.of(16, 30),
        endTime = LocalTime.of(17, 0),
        color = EventColorMint
    ),
    Event(
        id = 1,
        title = "Team Meeting",
        description = "Weekly sync with the dev team",
        startTime = LocalTime.of(10, 0),
        endTime = LocalTime.of(12, 0),
        color = EventColorSky
    ),
    Event(
        id = 2,
        title = "Lunch Break",
        description = "Quick meal at the café",
        startTime = LocalTime.of(12, 30),
        endTime = LocalTime.of(13, 30),
        color = EventColorSun
    ),
    Event(
        id = 3,
        title = "Gym Session",
        description = "Cardio and weights",
        startTime = LocalTime.of(18, 0),
        endTime = LocalTime.of(18, 10),
        color = EventColorLavender
    ),
    Event(
        id = 4,
        title = "Dinner with Friends",
        description = "Italian restaurant reservation",
        startTime = LocalTime.of(20, 0),
        endTime = LocalTime.of(22, 0),
        color = EventColorCrimson
    )
)

@SuppressLint("DefaultLocale")
@Composable
fun Timeline(
    events: List<Event>,
    isCurrentDay: Boolean,
    modifier: Modifier = Modifier,
    onEventClick: (Event) -> Unit
) {
    val totalMinutes = 24 * 60
    val density = LocalDensity.current
    val hourHeight = 60
    val scrollState = rememberScrollState()

    val uiEvents = getEventDimensions(events)

    val now = LocalTime.now()
    val currentMinuteOfDay = now.hour * 60 + now.minute

    val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    LaunchedEffect(isCurrentDay) {
        if (isCurrentDay) {
            scrollState.scrollTo(currentMinuteOfDay + 700)
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 10.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .height(totalMinutes.dp),
            verticalArrangement = Arrangement.Top
        ) {
            for (hour in 0..23) {
                Box(
                    modifier = Modifier
                        .height(hourHeight.dp)
                        .width(56.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = String.format("%02d:00", hour),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(totalMinutes.dp)
                .padding(top = 8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (hour in 0..23) {
                    val y = with(density) { (hour * 60).dp.toPx() }
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y)
                    )
                }
            }

            uiEvents.forEach { event ->
                EventCard(event = event, onClick = { onEventClick(event.event) })
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                if (isCurrentDay) {
                    val y = with(density) { currentMinuteOfDay.toFloat().dp.toPx() }
                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 8f
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = 16f,
                        center = Offset(0f, y)
                    )
                }
            }
        }
    }
}

fun getEventDimensions(events: List<Event>): List<UIEvent> {
    return events.map { event ->
        val startMinutes = (event.startTime?.toSecondOfDay() ?: 0) / 60
        val endMinutes = (event.endTime?.toSecondOfDay() ?: 0) / 60
        val duration = max(endMinutes - startMinutes, 10)

        UIEvent(
            event = event,
            top = startMinutes,
            height = duration
        )
    }
}
