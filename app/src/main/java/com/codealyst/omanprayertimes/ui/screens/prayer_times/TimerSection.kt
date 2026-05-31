package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.ui.theme.AdhanDark
import com.codealyst.omanprayertimes.ui.theme.AdhanLight
import com.codealyst.omanprayertimes.ui.theme.IqamahDark
import com.codealyst.omanprayertimes.ui.theme.IqamahLight

@Composable
fun TimerSection(salahName: String, isAdhan: Boolean, secondsLeft: Int) {
    val isDarkTheme = isSystemInDarkTheme()
    val adhanColor = if (isDarkTheme) AdhanDark else AdhanLight
    val iqamahColor = if (isDarkTheme) IqamahDark else IqamahLight

    var label = salahName + (if (isAdhan) " Adhan " else " Iqamah ") + "in"
    var labelColor = if (isAdhan) adhanColor else iqamahColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(12.dp))

        Text(
            label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            formatTime(secondsLeft),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = labelColor
        )

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}

fun formatTime(seconds: Int): String {
    val totalSeconds = seconds.coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val remainingSeconds = totalSeconds % 60

    return if (hours > 0) {
        "${hours}h ${minutes}m"
    } else if (minutes > 0) {
        "${minutes}m"
    } else {
        "${remainingSeconds}s"
    }
}
