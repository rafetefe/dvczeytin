package io.github.rafetefe

import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

object OliveDataStorage {
    private const val PREFS_NAME = "olive_data_prefs"
    private const val KEY_TYPES = "olive_types"
    private const val KEY_CATEGORIES_MAP = "categories_map"

    /**
     * Saves the current olive types list and categories map to SharedPreferences.
     */
    fun saveOliveData(
        context: Context,
        oliveTypes: List<String>,
        categoriesMap: Map<String, List<String>>,
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val typesJson = JSONArray(oliveTypes).toString()

        val mapJson = JSONObject()
        categoriesMap.forEach { (type, categories) ->
            mapJson.put(type, JSONArray(categories))
        }

        prefs.edit {
            putString(KEY_TYPES, typesJson)
            putString(KEY_CATEGORIES_MAP, mapJson.toString())
        }
    }

    /**
     * Loads the saved olive types list and categories map from SharedPreferences.
     * Returns null if no saved configuration exists.
     */
    fun loadOliveData(context: Context): Pair<List<String>, Map<String, List<String>>>? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val typesStr = prefs.getString(KEY_TYPES, null) ?: return null
        val mapStr = prefs.getString(KEY_CATEGORIES_MAP, null) ?: return null

        return try {
            val typesArray = JSONArray(typesStr)
            val typesList = mutableListOf<String>()
            for (i in 0 until typesArray.length()) {
                typesList.add(typesArray.getString(i))
            }

            val mapObj = JSONObject(mapStr)
            val map = mutableMapOf<String, List<String>>()
            val keys = mapObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val catArray = mapObj.getJSONArray(key)
                val catList = mutableListOf<String>()
                for (j in 0 until catArray.length()) {
                    catList.add(catArray.getString(j))
                }
                map[key] = catList
            }

            Pair(typesList, map)
        } catch (_: Exception) {
            null
        }
    }
}
