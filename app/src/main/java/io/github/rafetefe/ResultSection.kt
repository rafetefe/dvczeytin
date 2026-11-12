package io.github.rafetefe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Calculates the result: measure quantity multiplied by the unit price extracted from the selected category label.
 * Formats the output number with a space as a thousands separator for readability.
 * Returns "Bilinmeyen fiyat" if the right side of the category label is non-numeric or "?".
 */
fun calculateResult(measureText: String, categoryLabel: String?): String {
    if (categoryLabel.isNullOrBlank()) {
        return "Bilinmeyen fiyat"
    }

    val parts = categoryLabel.split(":", ";")
    if (parts.size < 2) {
        return "Bilinmeyen fiyat"
    }

    val rightPart = parts[1].trim()
    if (rightPart.contains("?") || rightPart.equals("yağlık", ignoreCase = true)) {
        return "Bilinmeyen fiyat"
    }

    val unitPrice = rightPart.replace(',', '.').toDoubleOrNull()
        ?: return "Bilinmeyen fiyat"

    val quantity = measureText.replace(',', '.').toDoubleOrNull() ?: 0.0
    val total = quantity * unitPrice

    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formattedNumber = if ((total % 1.0) == 0.0) {
        DecimalFormat("#,##0", symbols).format(total)
    } else {
        DecimalFormat("#,##0.##", symbols).format(total)
    }

    return "$formattedNumber TL"
}

/**
 * A section displaying the calculation result below MeasureInputSection.
 *
 * @param measureValue The current measure value string from MeasureInputSection.
 * @param selectedCategory The currently selected category label string from CategorySelectionSection.
 * @param modifier Modifier to be applied to the section.
 */
@Composable
fun ResultSection(
    measureValue: String,
    selectedCategory: String?,
    modifier: Modifier = Modifier,
) {
    val resultText = calculateResult(measureValue, selectedCategory)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Sonuç",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultSectionPreview() {
    DVCZeytinTheme {
        ResultSection(
            measureValue = "1000",
            selectedCategory = "D : 45",
        )
    }
}
