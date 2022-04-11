package com.example.itemizer

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

class ItemManager {

    companion object {

        // Static Methods
        fun fetchRecords(urlString: String): MutableList<Item> {

            var items: MutableList<Item> = mutableListOf<Item>()

            try {
                // Read in JSON result string from URL
                val result = URL(urlString).readText()

                // Get array of item objects from JSON result string
                val gson = Gson()

                val listItemType = object : TypeToken<MutableList<Item>>() {}.type
                items = gson.fromJson<MutableList<Item>>(result, listItemType)
                items.removeAll { item: Item -> item.name == null || item.name.isEmpty() }

                // Sort array by list ID, then name
                items.sortBy { item -> item.name }
                items.sortBy { item -> item.listId }

            } catch(e: Exception) {
                e.printStackTrace()
                throw e
            }

            return items
        }
    }
}