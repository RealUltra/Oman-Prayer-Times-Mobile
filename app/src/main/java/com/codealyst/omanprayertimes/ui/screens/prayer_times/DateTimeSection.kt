package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.ui.components.TiledBox
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@Composable
fun DateTimeSection(now: ZonedDateTime, modifier: Modifier = Modifier) {
    val timeText = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"))
    val dateText = now.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy"))
    val hijriDateText = formatHijrahDate(HijrahDate.from(now))

    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    TiledBox(
        modifier = modifier
            .fillMaxWidth(),
        resourceId = R.drawable.texture,
        alpha = 0.1f,
        offset = Offset(0.0f, -100f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                timeText,
                style = fonts.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                dateText,
                style = fonts.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                hijriDateText,
                style = fonts.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.tertiary
                )
            )
        }
    }
}

fun formatHijrahDate(hijrahDate: HijrahDate): String {
    val hijriMonths = listOf(
        "Muharram",
        "Safar",
        "Rabi al-Awwal",
        "Rabi al-Thani",
        "Jumada al-Awwal",
        "Jumada al-Thani",
        "Rajab",
        "Sha'ban",
        "Ramadan",
        "Shawwal",
        "Dhu al-Qi'dah",
        "Dhu al-Hijjah"
    )

    val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
    val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
    val year = hijrahDate.get(ChronoField.YEAR_OF_ERA)

    return "$day ${hijriMonths[month - 1]} $year"
}
