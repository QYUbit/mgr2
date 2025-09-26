package com.qyub.mgr2.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * WeekdaySelection: Multi- or single-select weekday picker
 *
 * @param selected initial set of selected days
 * @param onSelectionChange callback when selection changes
 * @param chipSize diameter of each circular chip
 * @param spacing horizontal spacing between chips
 * @param selectedColor background color when selected
 * @param unselectedColor background color when unselected
 * @param selectedTextColor text color for selected chips
 * @param unselectedTextColor text color for unselected chips
 * @param singleSelection if true, only one day can be selected at a time
 */
@Composable
fun WeekdaySelection(
    selected: Set<Int>,
    onSelectionChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    chipSize: Dp = 48.dp,
    spacing: Dp = 12.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.surface,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    singleSelection: Boolean = false
) {
    val weekDayLabels = mapOf(0 to "M", 1 to "T", 2 to "W", 3 to "T", 4 to "F", 5 to "S", 6 to "S")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekDayLabels.forEach { (index, label) ->
            val isSelected = selected.contains(index)

            val backgroundColor by animateColorAsState(targetValue = if (isSelected) selectedColor else unselectedColor,
                label = "bgColor"
            )
            val textColor by animateColorAsState(targetValue = if (isSelected) selectedTextColor else unselectedTextColor,
                label = "txtColor"
            )

            Box(
                modifier = Modifier
                    .size(chipSize)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable {
                        val newSet = if (singleSelection) {
                            if (isSelected) emptySet() else setOf(index)
                        } else {
                            selected.toMutableSet().apply {
                                if (isSelected) remove(index) else add(index)
                            }
                        }
                        onSelectionChange(newSet)
                    }
                    .semantics { contentDescription = "Week day ${label}, ${if (isSelected) "selected" else "not selected"}" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
