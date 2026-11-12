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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.ui.text.font.FontWeight

/**
 * Mapping from olive type to its category option labels.
 */
val categoriesByOliveType: Map<String, List<String>> = mapOf(
    "domat" to listOf("D : 45", "1: 35", "2 : 25", "3 : 20", "4 : 15", "5 : 10"),
    "trilye" to listOf("D : 110", "1: 80", "2 : 55", "3 : 30", "4 : 20", "Y : ?"),
    "trilyekizil" to listOf("D,1,2,3 : 30", "4 : 20", "Y : ?"),
    "edremit" to listOf("D,1 : 45", "2: 35", "3 : 25", "4 : 20"),
    "tekir" to listOf("D : 30", "1: 25", "2 : 20", "3 : 15"),
    "uslu" to listOf("iri: 15", "ince: yağlık"),
)

/**
 * Returns the list of categories for a given olive type.
 */
fun getCategoriesForType(type: String, map: Map<String, List<String>> = categoriesByOliveType): List<String> {
    return map[type] ?: emptyList()
}

/**
 * A section displaying a horizontal row of category buttons.
 * Behaves like a radio button group: only one option can be selected at a time,
 * and selecting a new option unselects the previous selection within this row.
 *
 * @param categories The list of category labels to display.
 * @param selectedCategory The currently selected category.
 * @param onCategorySelected Callback invoked when a new category is selected.
 * @param modifier Modifier to be applied to the row.
 */
@Composable
fun CategorySelectionRow(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        onCategorySelected(category)
                    }
                },
                modifier = Modifier.defaultMinSize(minHeight = 44.dp),
                label = {
                    Text(
                        text = category,
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
 * A container section that displays category selection buttons for explicit categories and selection.
 *
 * @param categories The list of category options to display.
 * @param selectedCategory The currently selected category option.
 * @param onCategorySelected Callback invoked when a category is selected.
 * @param modifier Modifier to be applied to the section.
 */
@Composable
fun CategorySelectionSection(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Kategori",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        CategorySelectionRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = modifier,
        )
    }
}

/**
 * A container section that displays category selection buttons based on the selected olive type.
 * Automatically updates category choices and selection when [selectedType] changes.
 *
 * @param selectedType The currently selected olive type.
 * @param modifier Modifier to be applied to the section.
 * @param onCategorySelected Callback invoked when a category is selected.
 */
@Composable
fun CategorySelectionSection(
    selectedType: String,
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit = {},
) {
    val categories = remember(selectedType) { getCategoriesForType(selectedType) }
    var selectedCategory by remember(selectedType) { mutableStateOf(categories.firstOrNull()) }

    CategorySelectionSection(
        categories = categories,
        selectedCategory = selectedCategory,
        onCategorySelected = { newSelection ->
            selectedCategory = newSelection
            onCategorySelected(newSelection)
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun CategorySelectionSectionPreview() {
    DVCZeytinTheme {
        CategorySelectionSection(selectedType = "domat")
    }
}
