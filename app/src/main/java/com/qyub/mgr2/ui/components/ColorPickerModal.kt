package com.qyub.mgr2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.qyub.mgr2.ui.theme.EventColorCrimson
import com.qyub.mgr2.ui.theme.EventColorEmerald
import com.qyub.mgr2.ui.theme.EventColorFlamingo
import com.qyub.mgr2.ui.theme.EventColorIndigo
import com.qyub.mgr2.ui.theme.EventColorLavender
import com.qyub.mgr2.ui.theme.EventColorLime
import com.qyub.mgr2.ui.theme.EventColorMagenta
import com.qyub.mgr2.ui.theme.EventColorMint
import com.qyub.mgr2.ui.theme.EventColorOrange
import com.qyub.mgr2.ui.theme.EventColorSky
import com.qyub.mgr2.ui.theme.EventColorSun
import com.qyub.mgr2.ui.theme.EventColorTurquoise
import com.qyub.mgr2.ui.theme.PrimaryLight

val colorLabels = mapOf(
    PrimaryLight to "Default",
    EventColorCrimson to "Crimson",
    EventColorOrange to "Orange",
    EventColorSun to "Sun",
    EventColorLime to "Lime",
    EventColorEmerald to "Emerald",
    EventColorMint to "Mint",
    EventColorTurquoise to "Turquoise",
    EventColorSky to "Sky",
    EventColorIndigo to "Indigo",
    EventColorLavender to "Lavender",
    EventColorMagenta to "Magenta",
    EventColorFlamingo to "Flamingo",
)

@Composable
fun ColorPickerDialog(
    colors: Map<Color, String> = colorLabels,
    selected: Color?,
    onSelect: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "Choose event color"
) {
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
                    items(colors.toList()) { (color, label) ->
                        ColorPickerRow(
                            color = color,
                            label = label,
                            isSelected = selected == color,
                            onClick = { onSelect(color) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorPickerRow(
    color: Color,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, role = Role.Button)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ringSize = 28.dp
        Box(
            modifier = Modifier
                .size(ringSize)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 4.dp else 3.dp,
                    color = color,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

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
