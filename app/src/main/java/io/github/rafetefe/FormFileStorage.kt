package io.github.rafetefe

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormFileStorage {
    private fun getStorageDir(context: Context): File {
        val dir = File(context.getExternalFilesDir(null), "saved_forms")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Saves the list of FormRow items to a text document named with the current date and time.
     * Returns the saved File, or null if items is empty.
     */
    fun saveForm(context: Context, items: List<FormRow>): File? {
        val defaultName = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
        return saveFormWithCustomName(context, defaultName, items)
    }

    /**
     * Saves the list of FormRow items to a text document with a custom file name.
     * Returns the saved File, or null if items is empty.
     */
    fun saveFormWithCustomName(context: Context, customName: String, items: List<FormRow>): File? {
        if (items.isEmpty()) return null

        val defaultName = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
        val sanitizedName = customName.trim().ifEmpty { defaultName }
        val fileName = if (sanitizedName.endsWith(".txt", ignoreCase = true)) sanitizedName else "$sanitizedName.txt"
        val file = File(getStorageDir(context), fileName)

        val headerDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val sb = StringBuilder()
        sb.appendLine("Tarih: $headerDate")
        sb.appendLine("----------------------------------------")
        sb.appendLine("Cins\tBoyut\tMiktar\tToplam")
        sb.appendLine("----------------------------------------")
        items.forEach { row ->
            sb.appendLine("${row.cins}\t${row.boyut}\t${row.miktar}\t${row.toplam}")
        }

        file.writeText(sb.toString())
        return file
    }

    /**
     * Retrieves the list of saved form files, sorted newest first.
     */
    fun listSavedFiles(context: Context): List<File> {
        val dir = getStorageDir(context)
        return dir.listFiles { _, name -> name.endsWith(".txt") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    /**
     * Reads a saved form text file and parses it back into a list of FormRow items.
     */
    fun loadFormFromFile(file: File): List<FormRow> {
        val rows = mutableListOf<FormRow>()
        if (!file.exists()) return rows

        val lines = file.readLines()
        var parsingData = false
        for (line in lines) {
            if (line.startsWith("Cins\t")) {
                parsingData = true
                continue
            }
            if (line.startsWith("---")) continue
            if (parsingData && line.isNotBlank()) {
                val parts = line.split("\t")
                if (parts.size >= 4) {
                    rows.add(
                        FormRow(
                            cins = parts[0],
                            boyut = parts[1],
                            miktar = parts[2],
                            toplam = parts[3],
                        ),
                    )
                }
            }
        }
        return rows
    }
}
