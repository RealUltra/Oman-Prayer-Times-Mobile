package com.codealyst.omanprayertimes.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsGroup(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme;
    val fonts = MaterialTheme.typography;

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            title,
            style = fonts.bodyLarge.copy(
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )

        Surface(
            modifier = modifier.fillMaxWidth(),
            color = colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, colorScheme.outlineVariant)
        ) {
            Column() { content() }
        }
    }
}