package io.github.rafetefe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * Floating popup dialog for entering a new Zeytin Tipi name.
 */
@Composable
fun AddTypeDialog(
    onDismiss: () -> Unit,
    onTypeAdded: (String) -> Unit,
) {
    var typeName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Yeni Zeytin Tipi Ekle",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            OutlinedTextField(
                value = typeName,
                onValueChange = { typeName = it },
                label = { Text("Zeytin Tipi Adı") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmed = typeName.trim()
                    if (trimmed.isNotBlank()) {
                        onTypeAdded(trimmed)
                        onDismiss()
                    }
                },
            ) {
                Text("Ekle", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        },
    )
}

/**
 * Floating popup dialog for adding a new category to a specific olive type.
 */
@Composable
fun AddCategoryDialog(
    oliveType: String,
    onDismiss: () -> Unit,
    onCategoryAdded: (key: String, price: String) -> Unit,
) {
    var catKey by remember { mutableStateOf("") }
    var catValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "$oliveType için yeni kategori",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = catKey,
                    onValueChange = { catKey = it },
                    label = { Text("Boyut/Etiket") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = catValue,
                    onValueChange = { catValue = it },
                    label = { Text("Fiyat") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val k = catKey.trim()
                    val v = catValue.trim()
                    if (k.isNotBlank()) {
                        onCategoryAdded(k, v)
                        onDismiss()
                    }
                },
            ) {
                Text("Ekle", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        },
    )
}

/**
 * Floating window dialog allowing users to update category prices, create/delete Zeytin Tipi options,
 * and delete individual categories.
 *
 * @param oliveTypes The current list of olive types.
 * @param categoriesMap The current map from olive type to category label strings.
 * @param onDismiss Request to close the dialog without saving.
 * @param onSave Callback executed when saving updated types and category values.
 */
@Composable
fun PriceUpdateDialog(
    oliveTypes: List<String>,
    categoriesMap: Map<String, List<String>>,
    onDismiss: () -> Unit,
    onSave: (newOliveTypes: List<String>, newCategoriesMap: Map<String, List<String>>) -> Unit,
) {
    var localTypes by remember { mutableStateOf(oliveTypes) }
    var localMap by remember { mutableStateOf(categoriesMap) }
    var selectedType by remember { mutableStateOf(localTypes.firstOrNull() ?: "") }

    var showAddTypeDialog by remember { mutableStateOf(value = false) }
    var showAddCategoryDialog by remember { mutableStateOf(value = false) }

    val currentCategories = localMap[selectedType] ?: emptyList()

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(0.85f),
        title = {
            Text(
                text = "Fiyat Güncelleme ve Tipi / Kategori Düzenleme",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                // Section 1: Olive Type Selection & Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "1. Zeytin Tipi Seçin",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedButton(
                        onClick = { showAddTypeDialog = true },
                        modifier = Modifier.padding(end = 6.dp),
                    ) {
                        Text("Yeni Tip Ekle")
                    }
                    if (selectedType.isNotBlank()) {
                        OutlinedButton(
                            onClick = {
                                val typeToRemove = selectedType
                                localTypes = localTypes.filter { it != typeToRemove }
                                localMap -= typeToRemove
                                selectedType = localTypes.firstOrNull() ?: ""
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error,
                            ),
                        ) {
                            Text("Tipi Sil")
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    localTypes.forEach { type ->
                        FilterChip(
                            selected = (type == selectedType),
                            onClick = { selectedType = type },
                            label = { Text(text = type) },
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Section 2: Categories & Price Editing / Deletion / Add New Category Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "2. $selectedType Kategorileri & Fiyatları",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    if (selectedType.isNotBlank()) {
                        OutlinedButton(
                            onClick = { showAddCategoryDialog = true },
                        ) {
                            Text("Yeni Kategori Ekle")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    itemsIndexed(currentCategories) { index, catLabel ->
                        val parts = catLabel.split(":", ";")
                        val keyPart = parts[0].trim()
                        val valPart = parts.getOrNull(1)?.trim() ?: ""

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = keyPart,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = " : ",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                            )
                            OutlinedTextField(
                                value = valPart,
                                onValueChange = { newVal ->
                                    val updatedLabel = "$keyPart : $newVal"
                                    val listCopy = currentCategories.toMutableList()
                                    listCopy[index] = updatedLabel
                                    localMap += (selectedType to listCopy)
                                },
                                modifier = Modifier.width(110.dp),
                                singleLine = true,
                            )
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .clickable {
                                        val updatedList = currentCategories.toMutableList()
                                        updatedList.removeAt(index)
                                        localMap += (selectedType to updatedList)
                                    }
                                    .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(localTypes, localMap)
                    onDismiss()
                },
            ) {
                Text("Kaydet", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        },
    )

    if (showAddTypeDialog) {
        AddTypeDialog(
            onDismiss = { showAddTypeDialog = false },
        ) { newType ->
            if (!localTypes.contains(newType)) {
                localTypes += newType
                localMap += (newType to emptyList())
                selectedType = newType
            }
        }
    }

    if (showAddCategoryDialog && selectedType.isNotBlank()) {
        AddCategoryDialog(
            oliveType = selectedType,
            onDismiss = { showAddCategoryDialog = false },
        ) { key, price ->
            val newLabel = "$key : $price"
            val updatedList = (localMap[selectedType] ?: emptyList()) + newLabel
            localMap += (selectedType to updatedList)
        }
    }
}
