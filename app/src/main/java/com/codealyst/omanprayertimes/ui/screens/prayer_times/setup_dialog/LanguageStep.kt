package com.codealyst.omanprayertimes.ui.screens.prayer_times.setup_dialog

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.os.LocaleListCompat
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions

@Composable
fun LanguageStep(modifier: Modifier = Modifier) {
    val languageLabels = stringArrayResource(R.array.languages)

    val fonts = MaterialTheme.typography

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(
            stringResource(R.string.language) + ":",
            style = fonts.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )

        Dropdown(
            options = listOf(
                DropdownOptions(languageLabels[0], ""),
                DropdownOptions(languageLabels[1], "en"),
                DropdownOptions(languageLabels[2], "ar"),
            ),
            selectedValue = getSelectedLanguageTag(),
            onOptionSelected = { language -> setLanguage(language) },
        )
    }
}

private fun getSelectedLanguageTag(): String {
    return AppCompatDelegate
        .getApplicationLocales()
        .get(0)
        ?.toLanguageTag() ?: ""
}

private fun setLanguage(language: String) {
    val locales =
        if (language.isBlank()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(language)
        }

    AppCompatDelegate.setApplicationLocales(locales)
}