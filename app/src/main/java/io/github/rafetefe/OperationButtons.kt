package io.github.rafetefe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme

/**
 * OperationButtons section placed under ResultSection.
 * Contains five hold-activated buttons expanding across the full available width:
 * - Geçmiş (1 second hold)
 * - Ekle (1 second hold)
 * - Print (1 second hold)
 * - Kaydet (1 second hold)
 * - Fiyat Günc. (1 second hold)
 */
@Composable
fun OperationButtons(
    onGecmisHold: () -> Unit,
    onEkleHold: () -> Unit,
    onPrintHold: () -> Unit,
    onKaydetHold: () -> Unit,
    onFiyatGuncHold: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "İşlemler",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            HoldButton(
                text = "Geçmiş",
                holdDurationMs = 1000L,
                onHoldComplete = onGecmisHold,
                modifier = Modifier.weight(1f),
            )
            HoldButton(
                text = "Ekle",
                holdDurationMs = 1000L,
                onHoldComplete = onEkleHold,
                modifier = Modifier.weight(1f),
            )
            HoldButton(
                text = "Print",
                holdDurationMs = 1000L,
                onHoldComplete = onPrintHold,
                modifier = Modifier.weight(1f),
            )
            HoldButton(
                text = "Kaydet",
                holdDurationMs = 1000L,
                onHoldComplete = onKaydetHold,
                modifier = Modifier.weight(1f),
            )
            HoldButton(
                text = "Fiyat Günc.",
                holdDurationMs = 1000L,
                onHoldComplete = onFiyatGuncHold,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OperationButtonsPreview() {
    DVCZeytinTheme {
        OperationButtons(
            onGecmisHold = {},
            onEkleHold = {},
            onPrintHold = {},
            onKaydetHold = {},
            onFiyatGuncHold = {},
        )
    }
}
