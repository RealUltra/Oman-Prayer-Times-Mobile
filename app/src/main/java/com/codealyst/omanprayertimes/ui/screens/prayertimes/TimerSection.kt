package com.codealyst.omanprayertimes.ui.screens.prayertimes

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.codealyst.omanprayertimes.features.api.dtos.DailyPrayerTimes
import com.codealyst.omanprayertimes.features.prayertimes.viewmodels.UiState
import com.codealyst.omanprayertimes.ui.theme.AdhanDark
import com.codealyst.omanprayertimes.ui.theme.AdhanLight
import com.codealyst.omanprayertimes.ui.theme.IqamahDark
import com.codealyst.omanprayertimes.ui.theme.IqamahLight
import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimerSection(
    modifier: Modifier = Modifier,
    timerMetadata: TimerMetadata? = null,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val adhanColor = if (isDarkTheme) AdhanDark else AdhanLight
    val iqamahColor = if (isDarkTheme) IqamahDark else IqamahLight

    val label: String;
    val labelColor = if (timerMetadata?.isAdhan ?: true) adhanColor else iqamahColor

    if (timerMetadata == null) {
        label = "";
    } else {
        val suffix =
            if (timerMetadata.isAdhan == null) "" else if (timerMetadata.isAdhan) " Adhan" else " Iqamah";
        label = "${timerMetadata.salahName}${suffix} in"
    }

    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography;

    val textureBitmap = ImageBitmap.imageResource(id = R.drawable.texture2);

    Box(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
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
            if (timerMetadata != null) {
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
                if (timerMetadata != null) formatTime(timerMetadata.secondsLeft) else "-",
                style = fonts.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = labelColor
            )
        }
    }
}

fun formatTime(seconds: Int): String {
    val totalSeconds = seconds.coerceAtLeast(0)

    return if (totalSeconds > 3600) {
        val roundedMinutes = (totalSeconds + 59) / 60
        val hours = roundedMinutes / 60
        val minutes = roundedMinutes % 60
        "${hours}h ${minutes}m"
    } else if (totalSeconds >= 60) {
        val minutes = (totalSeconds + 59) / 60
        "${minutes}m"
    } else {
        "${totalSeconds}s"
    }
}

data class TimerMetadata(
    val salahName: String,
    val isAdhan: Boolean?,
    val secondsLeft: Int,
)

fun getTimerMetadata(
    now: ZonedDateTime,
    state: UiState<DailyPrayerTimes>
): TimerMetadata? {
    if (state !is UiState.Success) {
        return null;
    }

    val dailyPrayerTimes = state.data
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val prayerTimes: List<Pair<String, String>> = listOf(
        "Fajr" to dailyPrayerTimes.fajrTime,
        "Shurooq" to dailyPrayerTimes.shurooqTime,
        "Dhuhr" to dailyPrayerTimes.dhuhrTime,
        "Asr" to dailyPrayerTimes.asrTime,
        "Maghrib" to dailyPrayerTimes.maghribTime,
        "Isha'a" to dailyPrayerTimes.ishaaTime,
    )

    var nextPrayerName = ""
    var nextPrayerTime: LocalTime? = null
    val currentTime = now.toLocalTime()

    for ((name, timeText) in prayerTimes) {
        val prayerTime = LocalTime.parse(timeText, timeFormatter)
        if (prayerTime.isAfter(currentTime)) {
            nextPrayerName = name
            nextPrayerTime = prayerTime
            break
        }
    }

    var nextPrayerDate = now.toLocalDate()

    if (nextPrayerTime == null) {
        nextPrayerName = "Fajr"
        nextPrayerTime = LocalTime.parse(dailyPrayerTimes.fajrTime, timeFormatter)
        nextPrayerDate = nextPrayerDate.plusDays(1)
    }

    val nextPrayerDateTime = nextPrayerDate
        .atTime(nextPrayerTime)
        .atZone(now.zone)

    val millisecondsLeft = Duration.between(now, nextPrayerDateTime).toMillis()
    val secondsLeft = ((millisecondsLeft + 999) / 1000).toInt()

    return TimerMetadata(
        salahName = nextPrayerName,
        isAdhan = if (nextPrayerName == "Shurooq") null else true,
        secondsLeft = secondsLeft,
    )
}
