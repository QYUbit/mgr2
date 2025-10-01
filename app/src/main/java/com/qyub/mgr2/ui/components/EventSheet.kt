package com.qyub.mgr2.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.qyub.mgr2.data.models.Event
import com.qyub.mgr2.data.models.NotificationType
import com.qyub.mgr2.ui.theme.PrimaryLight
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventBottomSheet(
    initialEvent: Event? = null,
    onSave: (Event) -> Unit,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var title by remember { mutableStateOf(initialEvent?.title ?: "") }
    var description by remember { mutableStateOf(initialEvent?.description ?: "") }
    var isRepeating by remember { mutableStateOf(initialEvent?.isRepeating ?: false) }
    val date by remember { mutableStateOf(initialEvent?.date ?: LocalDate.now()) }
    var startTime by remember { mutableStateOf(initialEvent?.startTime ?: LocalTime.of(0, 0)) }
    var endTime by remember { mutableStateOf(getEventEnd(initialEvent) ?: LocalTime.of(0, 0)) }
    var weekDays by remember { mutableStateOf(initialEvent?.repeatOn ?: emptyList()) }
    var color by remember { mutableStateOf(initialEvent?.color ?: PrimaryLight) }
    var hasReminder by remember { mutableStateOf(initialEvent?.hasNotification ?: false) }
    var reminderType by remember { mutableStateOf(initialEvent?.notificationType ?: NotificationType.REMINDER) }
    var reminderMinutes by remember { mutableIntStateOf(initialEvent?.notificationMinutes ?: 15) }

    var colorPickerOpen by remember { mutableStateOf(false) }
    var reminderPickerOpen by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val context = LocalContext.current

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(min = 500.dp, max = 700.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val canDelete = initialEvent != null && onDelete != null

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            // Kotlin compiler bad, so check again
                            if (canDelete && onDelete != null) {
                                onDelete()
                            }
                            onDismiss()
                        }
                ) {
                    Icon(
                        if (canDelete) Icons.Default.Delete else Icons.Default.Close,
                        contentDescription = if (canDelete) "Delete Event" else "Discard Changes",
                        tint = if (canDelete) Color.Red else MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    if (initialEvent == null) "Create new event" else "Edit Event",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(if (initialEvent == null) "Create" else "Save",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onSave(
                            Event(
                                id = initialEvent?.id ?: 0,
                                title = title,
                                description = description.ifEmpty { null },
                                isRepeating = isRepeating,
                                repeatOn = if (isRepeating) weekDays else emptyList(),
                                date = if (!isRepeating) date else null,
                                startTime = startTime,
                                duration = getDuration(startTime, endTime),
                                color = color,
                                hasNotification = hasReminder,
                                notificationType = reminderType,
                                notificationMinutes = reminderMinutes,
                            )
                        )
                        onDismiss()
                    }
                )
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Repeating?")
                Switch(checked = isRepeating, onCheckedChange = { isRepeating = it })
            }

            if (!isRepeating) {
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.clickable { datePickerDialog.show() },
                    enabled = false,
                    label = { Text("Date") },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick end time")
                    }
                )
            }

            fun openTimePicker(initial: LocalTime, onTimeSelected: (LocalTime) -> Unit) {
                TimePickerDialog(
                    context,
                    { _, hour, minute -> onTimeSelected(LocalTime.of(hour, minute)) },
                    initial.hour,
                    initial.minute,
                    true
                ).show()
            }

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                OutlinedTextField(
                    value = startTime.toString(),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .clickable { openTimePicker(startTime) { startTime = it } },
                    enabled = false,
                    label = { Text("Start time") }
                )

                OutlinedTextField(
                    value = endTime.toString(),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clickable { openTimePicker(endTime) { endTime = it } },
                    enabled = false,
                    label = { Text("End time") }
                )
            }

            if (isRepeating) {
                Text("Repeat on", color = MaterialTheme.colorScheme.onSurfaceVariant)

                WeekdaySelection(
                    selected = weekDays.toSet(),
                    onSelectionChange = { newDays -> weekDays = newDays.toList() },
                    chipSize = 32.dp,
                )
            }

            Row (
                modifier = Modifier
                    .clickable { colorPickerOpen = true },
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text("Event Appearance")
            }

            if (colorPickerOpen) {
                ColorPickerDialog(
                    selected = color,
                    onSelect = { selected ->
                        color = selected
                        colorPickerOpen = false
                    },
                    onDismissRequest = { colorPickerOpen = false }
                )
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Reminder",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Switch(
                        checked = hasReminder,
                        onCheckedChange = { hasReminder = it }
                    )
                }

                if (hasReminder) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTypeDialog = true }
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Notification type",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = formatNotificationType(reminderType),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { reminderPickerOpen = true }
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Reminder",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = formatNotificationTime(reminderMinutes),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            if (showTypeDialog) {
                NotificationTypeDialog(
                    currentType = reminderType,
                    onDismissRequest = { showTypeDialog = false },
                    onSelect = { type ->
                        reminderType = type
                        showTypeDialog = false
                    }
                )
            }

            if (reminderPickerOpen) {
                NotificationTimeDialog(
                    currentMinutes = reminderMinutes,
                    onDismissRequest = { reminderPickerOpen = false },
                    onSelect = { minutes ->
                        reminderMinutes = minutes
                        reminderPickerOpen = false
                    }
                )
            }
        }
    }
}

private fun getEventEnd(event: Event?): LocalTime? {
    if (event == null) return null
    return event.startTime?.plusMinutes((event.duration ?: 0).toLong())
}

private fun getDuration(start: LocalTime, end: LocalTime): Int {
    val startMinutes = start.toSecondOfDay() / 60
    val endMinutes = end.toSecondOfDay() / 60
    return endMinutes - startMinutes
}

private fun formatNotificationType(type: NotificationType): String {
    return when (type) {
        NotificationType.REMINDER -> "Reminder"
        NotificationType.ALARM -> "Alarm"
        NotificationType.POPUP -> "Popup"
    }
}

private fun formatNotificationTime(minutes: Int): String {
    return when (minutes) {
        0 -> "At start time"
        in 1..59 -> "$minutes min before"
        60 -> "1 hour before"
        in 61..1439 -> "${minutes / 60} hours before"
        1440 -> "1 day before"
        else -> "${minutes / 1440} days before"
    }
}
