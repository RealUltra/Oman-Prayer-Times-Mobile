package com.codealyst.omanprayertimes.ui.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codealyst.omanprayertimes.BuildConfig
import com.codealyst.omanprayertimes.R
import com.codealyst.omanprayertimes.features.settings.dtos.City
import com.codealyst.omanprayertimes.features.settings.viewmodels.SettingsViewModel
import com.codealyst.omanprayertimes.ui.components.Dropdown
import com.codealyst.omanprayertimes.ui.components.DropdownOptions
import com.codealyst.omanprayertimes.ui.components.ScreenHeader
import com.codealyst.omanprayertimes.ui.components.SearchableDropdown

const val PRIVACY_POLICY_URL =
    "https://realultra.github.io/Oman-Prayer-Times-Mobile/legal/privacy_policy.html"


const val GITHUB_REPO_URL = "https://github.com/RealUltra/Oman-Prayer-Times-Mobile"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onConfigureIqamahTimes: () -> Unit
) {
    // Retrieve cities list
    var cityNames = stringArrayResource(R.array.city_names)
    var cityIds = integerArrayResource(R.array.city_ids)
    val citiesList = cityNames.toCityDtos(cityIds)

    // Get app settings
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    val languageLabels = stringArrayResource(R.array.languages)
    val themeLabels = stringArrayResource(R.array.themes)

    val uriHandler = LocalUriHandler.current

    val colorScheme = MaterialTheme.colorScheme
    val fonts = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(title = stringResource(R.string.settings))

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsGroup(title = stringResource(R.string.general)) {
                SettingsRow(title = stringResource(R.string.city)) {
                    SearchableDropdown(
                        options = citiesList.map { c -> DropdownOptions(c.cityName, c.cityId) },
                        selectedValue = settings.cityId,
                        onOptionSelected = { cityId -> settingsViewModel.setCityId(cityId) },
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = colorScheme.outlineVariant)

                SettingsRow(title = stringResource(R.string.language)) {
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

                HorizontalDivider(thickness = 1.dp, color = colorScheme.outlineVariant)

                SettingsRow(title = stringResource(R.string.theme)) {
                    Dropdown(
                        options = listOf(
                            DropdownOptions(themeLabels[0], ""),
                            DropdownOptions(themeLabels[1], "light"),
                            DropdownOptions(themeLabels[2], "dark")
                        ),
                        selectedValue = settings.theme,
                        onOptionSelected = { theme -> settingsViewModel.setTheme(theme) },
                    )
                }
            }

            SettingsGroup(title = stringResource(R.string.prayer_setup)) {
                SettingsRow(title = stringResource(R.string.iqamah_times)) {
                    Text(
                        stringResource(R.string.configure),
                        style = fonts.bodyMedium,
                        color = colorScheme.primary,
                        modifier = Modifier.clickable(
                            enabled = true,
                            onClick = onConfigureIqamahTimes
                        )
                    )
                }
            }

            SettingsGroup(title = stringResource(R.string.legal)) {
                SettingsRow(title = stringResource(R.string.privacy_policy)) {
                    Text(
                        stringResource(R.string.show),
                        style = fonts.bodyMedium,
                        color = colorScheme.primary,
                        modifier = Modifier.clickable(
                            enabled = true,
                            onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) })
                    )
                }
            }

            SettingsGroup(title = stringResource(R.string.about)) {
                SettingsRow(title = stringResource(R.string.version)) {
                    Text(
                        stringResource(R.string.version_name, BuildConfig.VERSION_NAME),
                        style = fonts.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = colorScheme.outlineVariant)

                SettingsRow(title = stringResource(R.string.source_code)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_link),
                        contentDescription = stringResource(R.string.github_repository),
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = { uriHandler.openUri(GITHUB_REPO_URL) })
                    )
                }
            }
        }
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

private fun Array<String>.toCityDtos(cityIds: IntArray): List<City> {
    require(size == cityIds.size)
    return mapIndexed { index, cityName ->
        City(
            cityName = cityName,
            cityId = cityIds[index]
        )
    }.sortedBy { it.cityName }
}
