package com.qyub.mgr2.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.qyub.mgr2.data.models.Event
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
    var endTime by remember { mutableStateOf(initialEvent?.endTime ?: LocalTime.of(0, 0)) }
    var weekDays by remember { mutableStateOf(initialEvent?.repeatOn ?: emptyList()) }

    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                //.height(800.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                if (initialEvent == null) "Create new event" else "Edit Event",
                style = MaterialTheme.typography.titleLarge
            )

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
                WeekdaySelection(
                    selected = weekDays.toSet(),
                    onSelectionChange = { newDays -> weekDays = newDays.toList() }
                )
            }

            Button(
                onClick = {
                    onSave(
                        Event(
                            title = title,
                            description = description.ifEmpty { null },
                            isRepeating = isRepeating,
                            repeatOn = initialEvent?.repeatOn ?: emptyList(),
                            date = date,
                            startTime = startTime,
                            endTime = endTime,
                            colorHex = initialEvent?.colorHex
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            if (initialEvent != null && onDelete != null) {
                TextButton(
                    onClick = {
                        onDelete()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
