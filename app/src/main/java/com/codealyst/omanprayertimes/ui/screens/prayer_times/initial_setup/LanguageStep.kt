package com.codealyst.omanprayertimes.ui.screens.prayer_times.initial_setup

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownWidth
import com.codealyst.omanprayertimes.ui.components.DropdownOptions

@Composable
fun LanguageStep(modifier: Modifier = Modifier) {
    val languageLabels = stringArrayResource(R.array.languages)

    SetupStepContent(
        title = stringResource(R.string.language_setup_title),
        description = stringResource(R.string.language_setup_description),
        modifier = modifier
    ) {
        Dropdown(
            options = listOf(
                DropdownOptions(languageLabels[0], ""),
                DropdownOptions(languageLabels[1], "en"),
                DropdownOptions(languageLabels[2], "ar"),
            ),
            modifier = Modifier.fillMaxWidth(),
            width = DropdownWidth.Fill,
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
