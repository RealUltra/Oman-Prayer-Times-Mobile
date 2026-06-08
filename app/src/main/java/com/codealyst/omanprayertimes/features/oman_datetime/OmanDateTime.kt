package com.codealyst.omanprayertimes.features.oman_datetime

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

val omanZone = ZoneId.of("Asia/Muscat")

fun getOmanTime(): LocalTime = LocalTime.now(omanZone)
fun getOmanDate(): LocalDate = LocalDate.now(omanZone)
fun getOmanDateTime(): ZonedDateTime = ZonedDateTime.now(omanZone)