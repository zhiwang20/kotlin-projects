package com.example.itemizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*  //in build.gradle: id 'kotlin-android-extensions'
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    // Attributes

    // Variables
    private lateinit var itemAdapter: ItemAdapter

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemAdapter = ItemAdapter(mutableListOf())

        //don't need findViewById; in build.gradle: id 'kotlin-android-extensions'
        itemListRecyclerView.adapter = itemAdapter
        itemListRecyclerView.layoutManager = LinearLayoutManager(this)
        itemsLoadingProgressBar.visibility = View.VISIBLE

        // Do async data fetch; Anko's doAsync
        doAsync {

            try {
                // Read in JSON result string from URL
                val urlString: String = PropertyManager.getProperty("itemsUrl", baseContext)
                val items: MutableList<Item> = ItemManager.fetchRecords(urlString)

                // Remove those items with null or empty strings
                //items.removeAll { item: Item -> item.name == null || item.name.isEmpty() }
                // Sort array by list ID, then name
                //items.sortBy { item -> item.name }
                //items.sortBy { item -> item.listId }

                // Anko's uiThread
                uiThread {
                    // Add array into adapter
                    itemAdapter.setItems(items)   //itemAdapter's setItems
                }
            } catch(e: Exception) {
                uiThread {
                    e.message?.let { it1 -> toast(it1) }
                    e.printStackTrace()
                }
            } finally {
                uiThread {
                    itemsLoadingProgressBar.visibility = View.GONE
                }
            }
        }
    }
}