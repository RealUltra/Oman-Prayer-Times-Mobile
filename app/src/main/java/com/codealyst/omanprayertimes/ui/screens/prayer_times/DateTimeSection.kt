package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@Composable
fun DateTimeSection() {
    val omanZone = ZoneId.of("Asia/Muscat")
    var now by remember { mutableStateOf(ZonedDateTime.now(omanZone)) }

    LaunchedEffect(Unit) {
        while (true) {
            now = ZonedDateTime.now(omanZone)
            delay(1000)
        }
    }

    val timeText = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"))
    val dateText = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val hijriDateText = formatHijrahDate(HijrahDate.from(now))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            timeText,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            dateText,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            hijriDateText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
        )
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
