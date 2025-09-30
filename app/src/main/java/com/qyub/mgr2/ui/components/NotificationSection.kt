package com.qyub.mgr2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationSettingsSection(
    hasNotification: Boolean,
    notificationMinutes: Int,
    onNotificationChanged: (Boolean) -> Unit,
    onMinutesChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
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
                checked = hasNotification,
                onCheckedChange = onNotificationChanged
            )
        }

        if (hasNotification) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialog = true }
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
                        text = formatNotificationTime(notificationMinutes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    if (showDialog) {
        NotificationTimeDialog(
            currentMinutes = notificationMinutes,
            onDismiss = { showDialog = false },
            onConfirm = { minutes ->
                onMinutesChanged(minutes)
                showDialog = false
            }
        )
    }
}

@Composable
private fun NotificationTimeDialog(
    currentMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var selectedMinutes by remember { mutableIntStateOf(currentMinutes) }

    val options = listOf(
        0 to "At start time",
        5 to "5 minutes before",
        10 to "10 minutes before",
        15 to "15 minutes before",
        30 to "30 minutes before",
        60 to "1 hour before",
        120 to "2 hours before",
        1440 to "1 day before"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reminder") },
        text = {
            Column {
                options.forEach { (minutes, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMinutes = minutes }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedMinutes == minutes,
                            onClick = { selectedMinutes = minutes }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(label)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedMinutes) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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