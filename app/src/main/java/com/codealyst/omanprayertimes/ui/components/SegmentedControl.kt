package com.codealyst.omanprayertimes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

data class SegmentedControlOption<T>(
    val label: String,
    val value: T
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SegmentedControl(
    options: List<SegmentedControlOption<T>>,
    selectedValue: T,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    onOptionSelected: (T) -> Unit = {}
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        options.forEachIndexed { index, option ->
            SegmentedControlButton(
                option = option,
                selectedValue = selectedValue,
                index = index,
                count = options.size,
                enabled = enabled,
                labelStyle = labelStyle,
                onClick = { onOptionSelected(option.value) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SingleChoiceSegmentedButtonRowScope.SegmentedControlButton(
    option: SegmentedControlOption<T>,
    selectedValue: T,
    index: Int,
    count: Int,
    enabled: Boolean,
    labelStyle: TextStyle,
    onClick: () -> Unit
) {
    SegmentedButton(
        selected = option.value == selectedValue,
        onClick = onClick,
        enabled = enabled,
        shape = SegmentedButtonDefaults.itemShape(index = index, count = count),
        label = {
            Text(
                text = option.label,
                maxLines = 1,
                style = labelStyle
            )
        }
    )
}
