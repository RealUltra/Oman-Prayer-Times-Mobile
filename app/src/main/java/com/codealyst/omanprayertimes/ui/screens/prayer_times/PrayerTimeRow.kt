package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.ui.components.TiledBox

@Composable
fun PrayerTimeRow(
    name: String,
    adhanTime: String,
    iqamahTime: String,
    modifier: Modifier = Modifier,
    isHeader: Boolean = false,
    highlighted: Boolean = false,
    isAdhanNext: Boolean = false,
) {
    val colorScheme = MaterialTheme.colorScheme;
    val adhanColor = colorScheme.tertiary
    val iqamahColor = colorScheme.primary

    val highlightColor = if (!highlighted) {
        Color.Transparent
    } else if (isAdhanNext) {
        colorScheme.tertiaryContainer
    } else {
        colorScheme.primaryContainer
    }

    TiledBox(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        highlightColor,
                        highlightColor.darken(0.5f),
                    )
                ),
                RoundedCornerShape(12.dp)
            ),
        resourceId = R.drawable.texture3,
        alpha = if (highlighted) 0.1f else 0.0f,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(vertical = if (!isHeader) 16.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = adhanTime,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = if (!isHeader) adhanColor else colorScheme.onSurface,
            )

            Text(
                text = iqamahTime,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = if (!isHeader) iqamahColor else colorScheme.onSurface,
            )
        }
    }
}

fun Color.darken(amount: Float): Color {
    return Color(
        red = red * (1f - amount),
        green = green * (1f - amount),
        blue = blue * (1f - amount),
        alpha = alpha
    )
}
