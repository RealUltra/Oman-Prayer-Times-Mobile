package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.dtos.City
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.TiledBox
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@Composable
fun DateTimeSection(now: ZonedDateTime, modifier: Modifier = Modifier) {
    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    // Get selected city
    var cityNames = stringArrayResource(R.array.city_names)
    var cityIds = integerArrayResource(R.array.city_ids)
    val selectedCity = cityNames.getCityById(settings.cityId, cityIds)

    // Prepare date and time text
    val timeText = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"))
    val dateText = now.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy"))
    val hijriDateText =
        formatHijrahDate(HijrahDate.from(now), stringArrayResource(R.array.hijri_months))

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
                style = fonts.displaySmall.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    dateText,
                    style = fonts.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.width(16.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = "City"
                )

                Spacer(Modifier.width(2.dp))

                Text(
                    selectedCity.cityName,
                    style = fonts.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                hijriDateText,
                style = fonts.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

private fun formatHijrahDate(hijrahDate: HijrahDate, hijriMonths: Array<String>): String {
    val day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
    val month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
    val year = hijrahDate.get(ChronoField.YEAR_OF_ERA)
    return "$day ${hijriMonths[month - 1]} $year"
}

private fun Array<String>.getCityById(cityId: Int, cityIds: IntArray): City {
    require(size == cityIds.size)
    val index = cityIds.indexOf(cityId).coerceAtLeast(0)
    return City(cityName = get(index), cityId = cityIds[index])
}