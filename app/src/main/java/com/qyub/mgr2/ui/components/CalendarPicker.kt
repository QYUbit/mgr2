package com.qyub.mgr2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Month
import java.time.MonthDay
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil

@Composable
fun MonthDateDialog(
    modifier: Modifier = Modifier,
    title: String = "Select date",
    selectedDays: List<Int>,
    onSelect: (Int) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = modifier
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

                CalendarSelection(
                    selected = selectedDays,
                    onDayClick = onSelect
                )
            }
        }
    }
}

@Composable
fun YearDateDialog(
    modifier: Modifier = Modifier,
    title: String = "Select date",
    selectedDate: MonthDay,
    onSelect: (MonthDay) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = modifier
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

                CalendarSelection(
                    selected = listOf(selectedDate.dayOfMonth),
                    onDayClick = { onSelect(MonthDay.of(selectedDate.monthValue, it)) }
                )

                Row (
                    Modifier.horizontalScroll(rememberScrollState())
                ) {
                    for (i in 1..12) {
                        val month = Month.of(i)

                        TextButton(
                            onClick = { onSelect(MonthDay.of(month, selectedDate.dayOfMonth)) }
                        ) {
                            Text(month.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarSelection(
    modifier: Modifier = Modifier,
    dayCount: Int = 31,
    onDayClick: (Int) -> Unit = {},
    selected: List<Int> = emptyList(),
    semiSelected: List<Int> = emptyList()
) {
    val rows = ceil((dayCount / 7).toDouble()).toInt()

    Column (
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (i in 1..rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (j in 1..7) {
                    val dayNumber = i * 7 + j
                    Box(
                        Modifier
                            .clickable { onDayClick(dayNumber) }
                            .background(color = if (selected.contains(dayNumber)) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface)
                            .clip(CircleShape),
                            //.border()
                        contentAlignment = Alignment.Center
                    ) {
                        Text(dayNumber.toString())
                    }
                }
            }
        }
    }
}