package io.github.rafetefe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private fun parseTotalValue(toplamText: String): Double {
    val numericStr = toplamText
        .replace("TL", "")
        .replace(" ", "")
        .replace(',', '.')
        .trim()
    return numericStr.toDoubleOrNull() ?: 0.0
}

private fun formatGrandTotal(total: Double): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }
    val formatted = if ((total % 1.0) == 0.0) {
        DecimalFormat("#,##0", symbols).format(total)
    } else {
        DecimalFormat("#,##0.##", symbols).format(total)
    }
    return "$formatted TL"
}

/**
 * A table view component displayed on the right side of the screen with 4 columns:
 * "Cins", "Boyut", "Miktar", and "Toplam", along with a grand total calculation at the bottom.
 *
 * @param items List of FormRow data items.
 * @param modifier Modifier for layout.
 * @param onDeleteRow Callback invoked to delete a specific row.
 */
@Composable
fun FormTable(
    items: List<FormRow>,
    modifier: Modifier = Modifier,
    onDeleteRow: (FormRow) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Form Listesi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            // Table Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Cins",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = "Boyut",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.1f),
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = "Miktar (kg)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.7f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Toplam",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.1f),
                    textAlign = TextAlign.End,
                )
                Text(
                    text = "",
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Table Body
            if (items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Henüz eklenmiş bir veri yok.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    itemsIndexed(items, key = { _, row -> row.id }) { index, row ->
                        val backgroundColor = if ((index % 2) == 0) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backgroundColor)
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = row.cins,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start,
                            )
                            Text(
                                text = row.boyut,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1.1f),
                                textAlign = TextAlign.Start,
                            )
                            Text(
                                text = row.miktar,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(0.7f),
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = row.toplam,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1.1f),
                                textAlign = TextAlign.End,
                            )
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .clickable { onDeleteRow(row) }
                                    .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }

                // Grand Total Summary Section at the Bottom
                val grandTotal = items.sumOf { parseTotalValue(it.toplam) }
                val grandTotalFormatted = formatGrandTotal(grandTotal)

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Genel Toplam:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = grandTotalFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormTablePreview() {
    DVCZeytinTheme {
        FormTable(
            items = listOf(
                FormRow(cins = "domat", boyut = "D : 45", miktar = "10", toplam = "450 TL"),
                FormRow(cins = "trilye", boyut = "1: 80", miktar = "5", toplam = "400 TL"),
            ),
        )
    }
}
