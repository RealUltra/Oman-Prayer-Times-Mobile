package com.codealyst.omanprayertimes.ui.screens.prayer_times.setup_dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun IqamahConfigsStep(modifier: Modifier = Modifier, onConfigureIqamahTimes: () -> Unit) {
    val fonts = MaterialTheme.typography

    TextButton(onClick = onConfigureIqamahTimes) {
        Text(
            "Configure the iqamah times for your local mosque now?",
            style = fonts.bodyMedium,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
    }
}