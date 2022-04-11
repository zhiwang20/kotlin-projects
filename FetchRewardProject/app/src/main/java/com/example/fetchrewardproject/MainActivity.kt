package com.example.fetchrewardproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    var resultData: List<Item> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button1).setOnClickListener{
            onClick(it)
       }
    }

    private fun onClick(view: View) {
        findViewById<Button>(R.id.button1).text = "Loading data"
        retrieveData()
        val myAdapter = ItemAdapter(this@MainActivity, R.layout.list_of_item, resultData)
        val listView1 = findViewById<ListView>(R.id.listView1)
        listView1.adapter = myAdapter
    }

    private fun retrieveData() {
        Thread {
            resultData = getItems()
            runOnUiThread {
                findViewById<Button>(R.id.button1).text = "Press this button again to display the data"
            }
        }.start()
    }

    private fun getItems(): ArrayList<Item> {
        val itemsUrl = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json").readText()
        val itemsArray = JSONArray(itemsUrl)
        var itemsList = ArrayList<Item>()

        for (i in 0 until itemsArray.length() - 1) {
            val isValidName = itemsArray.getJSONObject(i).optString("name").toString()
            if (isValidName != "" && isValidName != "null") {
                itemsList.add(
                    0, Item(
                        itemsArray.getJSONObject(i).optString("id"),
                        itemsArray.getJSONObject(i).optString("listId"),
                        itemsArray.getJSONObject(i).optString("name")
                    )
                )
            }
        }

        itemsList = itemsList.sortedWith(compareBy<Item> { it.listId.toInt() }.thenBy { it.name })
            .toMutableList() as ArrayList<Item>

        return itemsList
    }
}