package com.example.fetch_reward_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fetch_reward_new.adapter.ListAdapter
import com.example.fetch_reward_new.models.Item
import org.json.JSONArray
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var listTable: HashMap<Int, MutableList<Item>>
    private val url = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listTable = HashMap<Int, MutableList<Item>>()
        setData()

    }

    fun setData(){
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object: StringRequest(Request.Method.GET, url, {
            response ->
            run {
                listTable = HashMap<Int, MutableList<Item>>()
                val jsonArray = JSONArray(response)
                loop@ for (i in 0 until jsonArray.length()) {
                    try {
                        val itemObject = jsonArray.getJSONObject(i)
                        val name = itemObject.getString("name")
                        val listId = itemObject.getInt("listId")
                        val id = itemObject.getInt("id")
                        Log.d("api", "$name, $listId, $id")
                        if (!checkItemArgumentsValid(name, listId, id))
                            continue@loop

                        val item = Item(name, id, listId)

                        if(!listTable.contains(item.listId))
                            listTable[listId] = mutableListOf<Item>()

                        listTable[listId]!!.add(item)

                    } catch (error: Exception) {
                        Log.d("api", "onErrorResponse: " + error.message)
                    }
                }
                val recycler_view = findViewById<RecyclerView>(R.id.recycler_view)
                recycler_view.adapter = ListAdapter(listTable)
                recycler_view.layoutManager = LinearLayoutManager(this)
                recycler_view.setHasFixedSize(true)

            }
        }, {
            error ->
            run {
                Log.d("api", "onErrorResponse: " + error.message)
            }
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json";
                headers["Content-Length"] = "96";
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun checkItemArgumentsValid(name: String?, listId: Int?, id: Int?): Boolean{
        if(name == null || name == "" || name == "null")
            return false
        if(listId == null)
            return false
        if(id == null)
            return false

        return true
    }
}