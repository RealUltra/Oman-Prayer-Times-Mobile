package com.codealyst.omanprayertimes.ui.screens.iqamah_times

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.features.settings.IqamahMode
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions

@Composable
fun IqamahPrayerCard(
    prayerName: String,
    initialMinutesAfterAdhan: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography
    var mode by remember { mutableStateOf(IqamahMode.AFTER_ADHAN) }
    val contentAlpha = if (enabled) 1f else 0.54f

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = colorScheme.surfaceContainer,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(contentAlpha),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    prayerName,
                    style = fonts.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.onSurface
                )

                Dropdown(
                    options = listOf(
                        DropdownOptions("After adhan", IqamahMode.AFTER_ADHAN),
                        DropdownOptions("Exact time", IqamahMode.EXACT_TIME)
                    ),
                    selectedValue = mode,
                    enabled = enabled,
                    collapsedTextStyle = fonts.bodyMedium,
                    expandedTextStyle = fonts.bodyMedium,
                    onOptionSelected = { mode = it }
                )
            }

            HorizontalDivider(color = colorScheme.outlineVariant)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(contentAlpha),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeSelector(
                    mode = mode,
                    initialMinutesAfterAdhan = initialMinutesAfterAdhan,
                    enabled = enabled
                )
            }
        }
    }
}
