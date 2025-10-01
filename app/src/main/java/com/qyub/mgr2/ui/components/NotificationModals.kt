package com.qyub.mgr2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.qyub.mgr2.data.models.NotificationType

@Composable
fun NotificationTypeDialog(
    currentType: NotificationType?,
    onSelect: (NotificationType) -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "Select notification type"
) {
    val options = listOf(
        NotificationType.REMINDER to "Reminder",
        NotificationType.ALARM to "Alarm",
        NotificationType.POPUP to "Popup"
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .heightIn(max = 420.dp)
                ) {
                    items(options.toList()) { (type, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { onSelect(type) }, role = Role.Button)
                                .padding(horizontal = 18.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentType == type,
                                onClick = {}
                            )

                            Spacer(modifier = Modifier.width(18.dp))

                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationTimeDialog(
    currentMinutes: Int?,
    onSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "Select reminder"
) {
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

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .heightIn(max = 420.dp)
                ) {
                    items(options.toList()) { (minutes, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { onSelect(minutes) }, role = Role.Button)
                                .padding(horizontal = 18.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentMinutes == minutes,
                                onClick = {}
                            )

                            Spacer(modifier = Modifier.width(18.dp))

                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
