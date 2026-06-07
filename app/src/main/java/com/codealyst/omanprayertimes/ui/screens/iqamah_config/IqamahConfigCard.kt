package com.codealyst.omanprayertimes.ui.screens.iqamah_config

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahConfig
import com.codealyst.omanprayertimes.features.settings.dtos.IqamahMode
import com.codealyst.omanprayertimes.features.settings.dtos.PrayerKeys
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions

@Composable
fun IqamahConfigCard(
    prayerKey: String,
    initialMode: String,
    initialMinutesAfterAdhan: String,
    initialExactTime: String,
    onChanged: (IqamahConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography
    val prayerName = prayerKey.toPrayerName()
    var mode by remember(initialMode) { mutableStateOf(initialMode) }
    var minutesAfterAdhan by remember(initialMinutesAfterAdhan) {
        mutableStateOf(initialMinutesAfterAdhan.filter { it.isDigit() })
    }
    var exactTime by remember(initialExactTime) { mutableStateOf(initialExactTime) }

    fun emitConfig(
        nextMode: String = mode,
        nextMinutesAfterAdhan: String = minutesAfterAdhan,
        nextExactTime: String = exactTime
    ) {
        onChanged(
            IqamahConfig(
                prayerKey = prayerKey,
                mode = nextMode,
                minutesAfterAdhan = nextMinutesAfterAdhan.toIntOrNull(),
                exactTime = nextExactTime
            )
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = colorScheme.surfaceContainer,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.65f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    prayerName,
                    style = fonts.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )

                Dropdown(
                    modifier = Modifier.widthIn(min = 118.dp),
                    options = listOf(
                        DropdownOptions("After adhan", IqamahMode.AFTER_ADHAN),
                        DropdownOptions("Exact time", IqamahMode.EXACT_TIME)
                    ),
                    selectedValue = mode,
                    collapsedTextStyle = fonts.labelLarge,
                    expandedTextStyle = fonts.bodyMedium,
                    collapsedTextColor = colorScheme.primary,
                    onOptionSelected = {
                        mode = it
                        emitConfig(nextMode = it)
                    }
                )
            }

            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.55f))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TimeSelector(
                    mode = mode,
                    minutesAfterAdhan = minutesAfterAdhan,
                    exactTime = exactTime,
                    onMinutesAfterAdhanChange = {
                        minutesAfterAdhan = it
                        emitConfig(nextMinutesAfterAdhan = it)
                    },
                    onExactTimeChange = {
                        exactTime = it
                        emitConfig(nextExactTime = it)
                    }
                )
            }
        }
    }
}

private fun String.toPrayerName(): String {
    return when (this) {
        PrayerKeys.FAJR -> "Fajr"
        PrayerKeys.DHUHR -> "Dhuhr"
        PrayerKeys.ASR -> "Asr"
        PrayerKeys.MAGHRIB -> "Maghrib"
        PrayerKeys.ISHA -> "Isha'a"
        else -> this
    }
}
