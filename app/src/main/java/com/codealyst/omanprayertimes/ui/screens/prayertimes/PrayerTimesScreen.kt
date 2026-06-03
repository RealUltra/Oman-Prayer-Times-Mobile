package com.codealyst.omanprayertimes.ui.screens.prayertimes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.codealyst.omanprayertimes.features.prayertimes.viewmodels.PrayerTimesViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun PrayerTimesScreen(modifier: Modifier = Modifier) {
    var tableDate by rememberSaveable {
        mutableStateOf(LocalDate.now(ZoneId.of("Asia/Muscat")).toString())
    }

    val prayerTimesViewModel = hiltViewModel<PrayerTimesViewModel>();

    LaunchedEffect(tableDate) {
        prayerTimesViewModel.fetchPrayerTimesForDate(LocalDate.parse(tableDate));
    }

    val omanZone = ZoneId.of("Asia/Muscat")
    var now by remember { mutableStateOf(ZonedDateTime.now(omanZone)) }

    LaunchedEffect(Unit) {
        while (true) {
            now = ZonedDateTime.now(omanZone)
            delay(1000)
        }
    }

    val timerMetadata = getTimerMetadata(now, prayerTimesViewModel.state.value)

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DateTimeSection(now = now)

                TimerSection(timerMetadata = timerMetadata)

                PrayerTimesTable(
                    prayerTimesViewModel,
                    timerMetadata ?: TimerMetadata("", false, 0),
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                )
                DateSelector(onDateSelected = { date -> tableDate = date.toString() })

            }
        }
    }
}
