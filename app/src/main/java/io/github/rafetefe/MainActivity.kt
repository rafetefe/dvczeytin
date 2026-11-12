package io.github.rafetefe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.rafetefe.ui.theme.DVCZeytinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DVCZeytinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val initialSavedData = remember { OliveDataStorage.loadOliveData(context) }
    var oliveTypes by remember { mutableStateOf(initialSavedData?.first ?: defaultOliveTypes) }
    var categoriesMap by remember { mutableStateOf(initialSavedData?.second ?: categoriesByOliveType) }

    var selectedType by rememberSaveable { mutableStateOf(oliveTypes.firstOrNull() ?: "") }
    val categories = remember(selectedType, categoriesMap) { getCategoriesForType(selectedType, categoriesMap) }
    var selectedCategory by remember(selectedType, categoriesMap) { mutableStateOf(categories.firstOrNull()) }
    var measureValue by rememberSaveable { mutableStateOf("0") }

    val formItems = remember { mutableStateListOf<FormRow>() }
    var showHistoryDialog by remember { mutableStateOf(value = false) }
    var showPriceUpdateDialog by remember { mutableStateOf(value = false) }
    var showSaveFileDialog by remember { mutableStateOf(value = false) }

    fun resetSelections() {
        if (oliveTypes.isNotEmpty()) {
            selectedType = oliveTypes.first()
        }
        measureValue = "0"
    }

    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        // Left Side: Inputs, Calculation Result, and Operation Buttons (wider layout)
        Column(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
        ) {
            TypeSelectionSection(
                selectedType = selectedType,
                onTypeSelected = { newType ->
                    selectedType = newType
                },
                oliveTypes = oliveTypes,
            )
            CategorySelectionSection(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                },
            )
            MeasureInputSection(
                value = measureValue,
                onValueChange = { newValue ->
                    measureValue = newValue
                },
            )
            ResultSection(
                measureValue = measureValue,
                selectedCategory = selectedCategory,
            )
            OperationButtons(
                onGecmisHold = {
                    showHistoryDialog = true
                },
                onEkleHold = {
                    val result = calculateResult(measureValue, selectedCategory)
                    val newRow = FormRow(
                        cins = selectedType,
                        boyut = selectedCategory ?: "",
                        miktar = measureValue,
                        toplam = result,
                    )
                    formItems.add(newRow)
                    resetSelections()
                },
                onPrintHold = {
                    PdfPrintHelper.printForm(context, formItems)
                },
                onKaydetHold = {
                    if (formItems.isNotEmpty()) {
                        showSaveFileDialog = true
                    }
                },
                onFiyatGuncHold = {
                    showPriceUpdateDialog = true
                },
            )
        }

        // Vertical Divider line separating left and right sides
        VerticalDivider(modifier = Modifier.fillMaxHeight())

        // Right Side: 4-Column Form Table
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            FormTable(
                items = formItems,
                onDeleteRow = { row ->
                    formItems.remove(row)
                },
            )
        }
    }

    if (showHistoryDialog) {
        HistoryDialog(
            onDismiss = { showHistoryDialog = false },
        ) { selectedFile ->
            val loadedRows = FormFileStorage.loadFormFromFile(selectedFile)
            formItems.clear()
            formItems.addAll(loadedRows)
        }
    }

    if (showSaveFileDialog) {
        SaveFileDialog(
            onDismiss = { showSaveFileDialog = false },
            onConfirm = { customFileName ->
                if (formItems.isNotEmpty()) {
                    FormFileStorage.saveFormWithCustomName(context, customFileName, formItems)
                    formItems.clear()
                    resetSelections()
                }
            },
        )
    }

    if (showPriceUpdateDialog) {
        PriceUpdateDialog(
            oliveTypes = oliveTypes,
            categoriesMap = categoriesMap,
            onDismiss = { showPriceUpdateDialog = false },
            onSave = { newOliveTypes, newCategoriesMap ->
                oliveTypes = newOliveTypes
                categoriesMap = newCategoriesMap
                OliveDataStorage.saveOliveData(context, newOliveTypes, newCategoriesMap)
                if (selectedType !in newOliveTypes && newOliveTypes.isNotEmpty()) {
                    selectedType = newOliveTypes.first()
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DVCZeytinTheme {
        MainScreen()
    }
}
