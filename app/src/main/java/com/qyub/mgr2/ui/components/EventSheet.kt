package com.qyub.mgr2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.qyub.mgr2.data.models.NewEvent
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventBottomSheet(
    initialEvent: NewEvent? = null,
    onSave: (NewEvent) -> Unit,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var title by remember { mutableStateOf(initialEvent?.title ?: "") }
    var description by remember { mutableStateOf(initialEvent?.description ?: "") }
    var isRepeating by remember { mutableStateOf(initialEvent?.isRepeating ?: false) }
    var date by remember { mutableStateOf(initialEvent?.date ?: LocalDate.now()) }
    var startTime by remember { mutableStateOf(initialEvent?.startTime) }
    var endTime by remember { mutableStateOf(initialEvent?.endTime) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

            Button(onClick = { /* DatePicker */ }) {
                Text("Date: ${'$'}date")
            }

            Button(onClick = { /* TimePicker */ }) {
                Text("Start: ${startTime ?: "--:--"}")
            }

            Button(onClick = { /* TimePicker */ }) {
                Text("End: ${endTime ?: "--:--"}")
            }

            Button(
                onClick = {
                    onSave(
                        NewEvent(
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
