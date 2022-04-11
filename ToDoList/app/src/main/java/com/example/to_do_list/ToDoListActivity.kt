package com.example.to_do_list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.content.Context
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.util.*

/*
 * This app allows you to create a to-do list.
 * Note:
 * Adding: click 'ADD' button to add a new item
 * Complete: long-press an item to complete it and move it to done-list
 * Move to the top row: click CLOCK icon and move its row to the very first row
 * Remove: click the BIN icon to remove it from current list
 * Scrolling: scroll to view more items
 * Persistent: list are persistently stored
 */

class ToDoListActivity : AppCompatActivity() {
    private val todo_items: MutableList<String> = LinkedList()
    private val done_items: MutableList<String> = LinkedList()
    private lateinit var todoListAdapter: ArrayAdapter<String>
    private lateinit var doneListAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)
        todoListAdapter = ArrayAdapter(this, R.layout.list_item, R.id.item_content, todo_items)
        doneListAdapter = ArrayAdapter(this, R.layout.finished_list, R.id.item_content, done_items)
        findViewById<ListView>(R.id.todo_list).adapter = todoListAdapter
        findViewById<ListView>(R.id.done_list).adapter = doneListAdapter

        findViewById<ListView>(R.id.todo_list).setOnItemLongClickListener{ parent, view, position, id ->
            onLongClickList(position, view)
        }
    }

    // add an item to to-do list; when "add" button is clicked
    fun onClickAdd(view: View){
        val item:String = findViewById<EditText>(R.id.add_item).text.toString()  //get text from EditText
        if(item.isEmpty()){
            return
        }
        todo_items.add(item)
        todoListAdapter.notifyDataSetChanged()
        findViewById<EditText>(R.id.add_item).setText("")  //make EditText box empty
    }

    // function called in the itemLongClickListener
    // this function gets the item that was long-pressed and move it to done list
    fun onLongClickList(position: Int, view: View): Boolean{
        val textview = findViewById<TextView>(R.id.item_content)  //view.findViewById  //from list_item.xml
        done_items.add(textview.text.toString())  //move item to done_list
        todo_items.removeAt(position)             //remove item from todo_list
        todoListAdapter.notifyDataSetChanged()
        doneListAdapter.notifyDataSetChanged()
        return true
    }

    // helper
    // when an bin or clock icon is clicked, get the selected row, then get its item_content
    fun getString(view: View): String{
        val row: RelativeLayout = view.parent as RelativeLayout   //view.getParent()
        //val textview: TextView = row.getChildAt(1) as TextView
        val text: String = row.findViewById<TextView>(R.id.item_content).text.toString()
        return text
    }

    //onClick for list_item.xml
    // called when the bin icon of to-do list is clicked
    fun deleteFromTodoList(view: View){
        val str = getString(view)
        todo_items.remove(str)
        todoListAdapter.notifyDataSetChanged()
    }

    //onClick for finished_list.xml
    // called when the bin icon of done list is clicked
    fun deleteFromDoneList(view: View){
        val str = getString(view)
        done_items.remove(str)
        doneListAdapter.notifyDataSetChanged()
    }

    //onClick for list_item.xml
    // called when the clock icon is clicked, move the item to the head of the list
    fun moveToTopRow(view: View){
        val str: String = getString(view)   //get row's item
        todo_items.remove(str)      //move this row to the very first row; other rows all have index minus one
        todo_items.add(0, str)
        todoListAdapter.notifyDataSetChanged()
    }


    // store the two list into interior storage when stops the activity
    override fun onStop() {
        super.onStop()
        Log.d("Wang", "stop")
        writeToFile(todo_items, "todo.txt")
        writeToFile(done_items, "done.txt")
        todo_items.clear()
        done_items.clear()
    }

    // read strings to the two list when start the activity
    override fun onStart() {
        super.onStart()
        val todoListDir = "$filesDir/todo.txt"   /* filesDir.toString() + "/" + "todo.txt" */
        val doneListDir = "$filesDir/done.txt"
        val todoList = readFile(todoListDir)
        val doneList = readFile(doneListDir)
        for(item in todoList){
            Log.d("addFile", item)
            todo_items.add(item)
        }
        for(item in doneList){
            done_items.add(item)
        }
    }

    // helper, read each line of a file into a list
    private fun readFile(filename: String): List<String>{
        val file = File(filename)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return file.readLines()
    }

    // helper, create and write a list to a file
    private fun writeToFile(list: MutableList<String>, filename: String) {
        val fn = "$filesDir/$filename"
        val file = File(fn)
        try {
            file.createNewFile()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            val outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            for(item in list) {
                Log.d("writeFile", item)
                outputStream.write((item + "\n").toByteArray())
            }
            outputStream.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}
