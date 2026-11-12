package io.github.rafetefe

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme
import java.util.Locale

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.ui.text.font.FontWeight

private fun isValidNonNegativeInput(text: String): Boolean {
    if (text.contains("-")) return false
    val parsed = text.toDoubleOrNull() ?: return true
    return parsed >= 0.0
}

/**
 * Helper function to apply an increment/decrement delta to a float number formatted string.
 * Clamps the resulting value so it never goes below 0.
 */
private fun applyDelta(currentText: String, delta: Double): String {
    val currentValue = currentText.replace(',', '.').toDoubleOrNull() ?: 0.0
    val newValue = (currentValue + delta).coerceAtLeast(0.0)
    return if ((newValue % 1.0) == 0.0) {
        newValue.toLong().toString()
    } else {
        String.format(Locale.US, "%.2f", newValue).trimEnd('0').trimEnd('.')
    }
}

/**
 * A section providing a numeric float input with step adjustment buttons (-10, -5, -1, +1, +5, +10)
 * and a CLR button to reset the value to 0.
 * Converts comma (,) decimal separators to dots (.) and prevents input from going below 0.
 *
 * @param value The current string representation of the numeric input.
 * @param onValueChange Callback invoked when the input value changes via keyboard or step buttons.
 * @param modifier Modifier to be applied to the section.
 */
@Composable
fun MeasureInputSection(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Miktar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = { onValueChange("0") },
                modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(
                    text = "CLR",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            val decrementSteps = listOf(-10.0 to "-10", -5.0 to "-5", -1.0 to "-1")
            val incrementSteps = listOf(1.0 to "+1", 5.0 to "+5", 10.0 to "+10")

            decrementSteps.forEach { (delta, label) ->
                OutlinedButton(
                    onClick = { onValueChange(applyDelta(value, delta)) },
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            OutlinedTextField(
                value = value,
                onValueChange = { newText ->
                    val formattedText = newText.replace(',', '.')
                    if (isValidNonNegativeInput(formattedText)) {
                        onValueChange(formattedText)
                    }
                },
                modifier = Modifier
                    .defaultMinSize(minHeight = 48.dp)
                    .widthIn(min = 100.dp, max = 140.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
            )

            incrementSteps.forEach { (delta, label) ->
                OutlinedButton(
                    onClick = { onValueChange(applyDelta(value, delta)) },
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

/**
 * Stateful overload of MeasureInputSection.
 */
@Composable
fun MeasureInputSection(
    modifier: Modifier = Modifier,
    initialValue: String = "0",
    onValueChange: (String) -> Unit = {},
) {
    var valueText by rememberSaveable { mutableStateOf(initialValue) }

    MeasureInputSection(
        value = valueText,
        onValueChange = { newValue ->
            valueText = newValue
            onValueChange(newValue)
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun MeasureInputSectionPreview() {
    DVCZeytinTheme {
        MeasureInputSection()
    }
}
