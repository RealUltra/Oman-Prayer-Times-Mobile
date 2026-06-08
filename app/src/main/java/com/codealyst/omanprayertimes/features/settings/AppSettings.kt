package com.codealyst.omanprayertimes.features.settings

data class AppSettings(
    val cityId: Int = 0,
    val theme: String = "",
    val iqamahTimesEnabled: Boolean = false
)
