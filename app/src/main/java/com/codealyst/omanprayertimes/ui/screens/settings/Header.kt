package com.codealyst.omanprayertimes.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.ui.components.TiledBox

@Composable
fun Header(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    TiledBox(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.primaryContainer),
        resourceId = R.drawable.texture,
        alpha = 0.1f,
        offset = Offset(0.0f, -100f)
    ) {
        Text(
            "Settings",
            modifier = Modifier.padding(bottom = 16.dp, top = 64.dp, start = 20.dp),
            style = fonts.displaySmall.copy(fontWeight = FontWeight.Bold),
        )
    }
}