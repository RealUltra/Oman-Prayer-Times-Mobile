package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R

@Composable
fun NextEventTimer(
    modifier: Modifier = Modifier,
    nextEvent: EventInfo? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val adhanColor = colorScheme.tertiary
    val iqamahColor = colorScheme.primary

    val label: String;
    val labelColor = if (nextEvent?.isAdhan ?: true) adhanColor else iqamahColor

    if (nextEvent == null) {
        label = "";
    } else {
        val suffix =
            if (nextEvent.isShurooq) "" else if (nextEvent.isAdhan) " Adhan" else " Iqamah";
        label = "${nextEvent.salahName}${suffix} in"
    }

    val fonts = MaterialTheme.typography;

    val textureBitmap = ImageBitmap.imageResource(id = R.drawable.texture2);

    Box(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            .border(1.dp, colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .drawBehind {
                drawRect(
                    brush = ShaderBrush(
                        shader = ImageShader(
                            image = textureBitmap,
                            tileModeX = TileMode.Repeated,
                            tileModeY = TileMode.Repeated,
                        )
                    ),
                    colorFilter = ColorFilter.tint(
                        color = colorScheme.onBackground.copy(alpha = 0.0f),
                        blendMode = BlendMode.Modulate,
                    ),
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (nextEvent != null) {
                Text(
                    label,
                    style = fonts.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.outline
                    )
                )
                Spacer(Modifier.height(8.dp))
            }
            Text(
                nextEvent?.secondsLeft?.formatTime() ?: "-",
                style = fonts.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = labelColor
            )
        }
    }
}

fun Int.formatTime(): String {
    val totalSeconds = this.coerceAtLeast(0)

    return if (totalSeconds >= 3600) {
        // Greater than or equal to 1 hour.

        val roundedMinutes = (totalSeconds + 59) / 60
        val hours = roundedMinutes / 60
        val minutes = roundedMinutes % 60

        if (minutes == 0) {
            "${hours}h"
        } else {
            "${hours}h ${minutes}m"
        }

    } else if (totalSeconds >= 60) {
        // Less than 1 hour, more than or equal to 1 minute.
        val minutes = (totalSeconds + 59) / 60
        "${minutes}m"

    } else {
        // Under 60 seconds
        "${totalSeconds}s"
    }
}
