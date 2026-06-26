package com.codealyst.omanprayertimes.features.settings

data class AppSettings(
    val loaded: Boolean = false,
    val cityId: Int = 0,
    val theme: String = "",
    val iqamahTimesEnabled: Boolean = false,
    val completedSetupSteps: List<String> = emptyList()
)
