package com.qyub.mgr2.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qyub.mgr2.data.models.Event
import java.time.LocalTime
import kotlin.math.max

data class UIEvent(
    val event: Event,
    val top: Int,
    val height: Int,
)

@Composable
fun Timeline(
    events: List<Event>,
    isCurrentDay: Boolean,
    modifier: Modifier = Modifier,
    onEventClick: (Event) -> Unit
) {
    val totalMinutes = 24 * 60
    val density = LocalDensity.current
    val totalHeightPx = totalMinutes.toFloat() // 1440px
    val totalHeightDp = with(density) { totalHeightPx.toDp() }
    val hourHeight = 60
    val scrollState = rememberScrollState()

    val sampleEvents = listOf<Event>(
        Event(
            id = 0,
            title = "Example",
            description = "go shopping",
            startTime = LocalTime.of(16, 30),
            endTime = LocalTime.of(17, 0),
            colorHex = "#0D990D"
        ),
        Event(
            id = 1,
            title = "Team Meeting",
            description = "Weekly sync with the dev team",
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(15, 0),
            colorHex = "#3A86FF" // Blue
        ),
        Event(
            id = 2,
            title = "Lunch Break",
            description = "Quick meal at the café",
            startTime = LocalTime.of(12, 30),
            endTime = LocalTime.of(13, 30),
            colorHex = "#FF8C00" // Orange
        ),
        Event(
            id = 3,
            title = "Gym Session",
            description = "Cardio and weights",
            startTime = LocalTime.of(18, 0),
            endTime = LocalTime.of(19, 30),
            colorHex = "#FF1493" // Pink
        ),
        Event(
            id = 4,
            title = "Dinner with Friends",
            description = "Italian restaurant reservation",
            startTime = LocalTime.of(20, 0),
            endTime = LocalTime.of(22, 0),
            colorHex = "#8B0000" // Dark Red
        )
    )

    val uiEvents = getEventDimensions(sampleEvents)

    val now = LocalTime.now()
    val currentMinuteOfDay = now.hour * 60 + now.minute

    val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .verticalScroll(scrollState)
                .height(totalMinutes.dp),
            verticalArrangement = Arrangement.Top
        ) {
            for (hour in 0..23) {
                Box(
                    modifier = Modifier
                        .height(hourHeight.dp)
                        .width(48.dp),
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
                .verticalScroll(scrollState)
                .height(totalMinutes.dp)
                .offset(y = 6.dp)
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

                if (isCurrentDay) {
                    val y = with(density) { currentMinuteOfDay.toFloat().dp.toPx() }
                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 2f
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = 6f,
                        center = Offset(0f, y)
                    )
                }
            }

            uiEvents.forEach { event ->
                Box(
                    modifier = Modifier
                        .offset(y = event.top.dp + 5.dp)
                        .fillMaxWidth()
                        .height(event.height.dp)
                        .background(
                            color = event.event.colorHex?.let {
                                Color(android.graphics.Color.parseColor(it))
                            } ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val eventInput = awaitPointerEvent()
                                    if (eventInput.changes.any { it.pressed }) {
                                        onEventClick(event.event)
                                        break
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event.event.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(2.dp)
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
        val duration = max(endMinutes - startMinutes, 30)

        UIEvent(
            event = event,
            top = startMinutes,
            height = duration
        )
    }
}
