package com.codealyst.omanprayertimes.ui.screens.prayer_times

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.ui.theme.AdhanDark
import com.codealyst.omanprayertimes.ui.theme.AdhanLight
import com.codealyst.omanprayertimes.ui.theme.IqamahDark
import com.codealyst.omanprayertimes.ui.theme.IqamahLight

@Composable
fun PrayerTimeRow(
    name: String,
    startTime: String,
    iqamahTime: String,
    isHeader: Boolean = false,
    highlighted: Boolean = false,
) {

    val isDarkTheme = isSystemInDarkTheme()
    val adhanColor = if (isDarkTheme) AdhanDark else AdhanLight
    val iqamahColor = if (isDarkTheme) IqamahDark else IqamahLight

    Box(
        Modifier
            .padding(horizontal = 12.dp)
            .background(
                if (highlighted) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = startTime,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = if (!isHeader) adhanColor else MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = iqamahTime,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = if (!isHeader) iqamahColor else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
