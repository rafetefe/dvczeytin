package io.github.rafetefe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.ui.text.font.FontWeight

/**
 * List of olive types in order.
 */
val defaultOliveTypes = listOf(
    "domat",
    "trilye",
    "trilyekizil",
    "edremit",
    "tekir",
    "uslu",
)

/**
 * A section displaying a horizontal row of 6 buttons for selecting a type.
 * Behaves like a radio button group: only one option can be selected at a time,
 * and selecting a new option unselects the previous selection.
 *
 * @param selectedType The currently selected type.
 * @param onTypeSelected Callback invoked when a new type is selected.
 * @param modifier Modifier to be applied to the row.
 * @param oliveTypes The list of button labels in order.
 */
@Composable
fun TypeSelectionRow(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    oliveTypes: List<String> = defaultOliveTypes,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(oliveTypes) { type ->
            val isSelected = type == selectedType
            FilterChip(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        onTypeSelected(type)
                    }
                },
                modifier = Modifier.defaultMinSize(minHeight = 44.dp),
                label = {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                },
            )
        }
    }
}

/**
 * A container section that displays the type selection row with controlled state.
 *
 * @param selectedType The currently selected type.
 * @param onTypeSelected Callback invoked when a new type is selected.
 * @param modifier Modifier to be applied to the section.
 * @param oliveTypes The list of button labels in order.
 */
@Composable
fun TypeSelectionSection(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    oliveTypes: List<String> = defaultOliveTypes,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Zeytin Tipi",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        TypeSelectionRow(
            selectedType = selectedType,
            onTypeSelected = onTypeSelected,
            oliveTypes = oliveTypes,
        )
    }
}

/**
 * A container section that displays the type selection row along with internal state management.
 */
@Composable
fun TypeSelectionSection(
    modifier: Modifier = Modifier,
    initialSelectedType: String = defaultOliveTypes.first(),
    oliveTypes: List<String> = defaultOliveTypes,
    onTypeSelected: (String) -> Unit = {},
) {
    var selectedType by rememberSaveable { mutableStateOf(initialSelectedType) }

    TypeSelectionSection(
        selectedType = selectedType,
        onTypeSelected = { newSelection ->
            selectedType = newSelection
            onTypeSelected(newSelection)
        },
        modifier = modifier,
        oliveTypes = oliveTypes,
    )
}

@Preview(showBackground = true)
@Composable
fun TypeSelectionRowPreview() {
    DVCZeytinTheme {
        TypeSelectionSection()
    }
}
