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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.prayer_times.titleRes

@Composable
fun NextEventTimer(
    modifier: Modifier = Modifier,
    nextEvent: EventInfo? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    val adhanColor = colorScheme.tertiary
    val iqamahColor = colorScheme.primary

    var label = "";
    val labelColor = if (nextEvent?.isAdhan ?: true) adhanColor else iqamahColor

    if (nextEvent != null) {
        label = if (nextEvent.isShurooq) {
            stringResource(R.string.next_event_sunrise_label)
        } else if (nextEvent.isAdhan) {
            stringResource(
                R.string.next_event_adhan_label,
                stringResource(nextEvent.prayerKey.titleRes())
            )
        } else {
            stringResource(
                R.string.next_event_iqamah_label,
                stringResource(nextEvent.prayerKey.titleRes())
            )
        }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .border(1.dp, colorScheme.outlineVariant, RoundedCornerShape(12.dp))
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
                nextEvent?.secondsLeft?.let { formatDurationText(it) } ?: "-",
                style = fonts.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = labelColor
            )
        }
    }
}

@Composable
private fun formatDurationText(secondsLeft: Int): String {
    val totalSeconds = secondsLeft.coerceAtLeast(0)
    val roundedMinutes = (totalSeconds + 59) / 60

    return if (roundedMinutes >= 60) {
        // Greater than or equal to 1 hour.
        val hours = roundedMinutes / 60
        val minutes = roundedMinutes % 60

        if (minutes == 0) {
            stringResource(R.string.duration_hours, hours)
        } else {
            stringResource(R.string.duration_hours_minutes, hours, minutes)
        }

    } else if (totalSeconds >= 60) {
        // Less than 1 hour, more than or equal to 1 minute.
        stringResource(R.string.duration_minutes, roundedMinutes)

    } else {
        // Under 60 seconds
        stringResource(R.string.duration_seconds, totalSeconds)
    }
}
