package com.qyub.mgr2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qyub.mgr2.data.models.Event
import java.time.format.DateTimeFormatter

@Composable
fun EventCard(event: UIEvent, onClick: (event: Event) -> Unit) {
    Box(
        modifier = Modifier
            .offset(y = event.top.dp)
            .fillMaxWidth(0.95f)
            .height(event.height.dp)
            .background(
                color = event.event.color ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick(event.event) },
        contentAlignment = Alignment.TopStart
    ) {
        Column (
            modifier = Modifier.padding(if (event.height <= 40) 6.dp else 12.dp)
        ) {
            if (event.height >= 30) {
                Text(
                    text = event.event.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                )
            }

            if (event.height >= 60) {
                Text(
                    text = if (event.event.startTime != null && event.event.endTime != null)
                        "${event.event.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${event.event.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                )
            }
        }
    }
}