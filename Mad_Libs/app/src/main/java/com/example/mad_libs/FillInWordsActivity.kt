package com.example.mad_libs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class FillInWordsActivity : AppCompatActivity() {
    val rawStory = Story()  //create a Story object
    var totalToFill = 0
    var indexToFill = 0

    // read the story template and set params
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_in_words)
        val storyName = intent.getStringExtra("storyName").toString()   //data sent from MainActivity
        rawStory.read(this, storyName)  //read function from Story class
        totalToFill = rawStory.placeholderCount   //placeholder haven't been replaced during this time, but _placeholder has finished storing, so it returns total number of items in _placeholder
        updatePlaceHolderCount()

        //show hint word of first placeholder initially
        if(indexToFill < totalToFill){
            Log.d("wang2", "$indexToFill and  $totalToFill")
            findViewById<EditText>(R.id.input_word).setHint("${rawStory.placeholders.get(indexToFill)}")  //example: adjective from <adjective> stored as hint word
        }
        //Log.d("output", rawStory.toString())
    }

    // onClick button "Ok" to fill in a word and update display; button from activity_fill_in_words.xml
    fun inputWord(view: View){
        val inputWord = findViewById<EditText>(R.id.input_word).text.toString()
        if(!inputWord.isEmpty() && indexToFill < totalToFill){
            Log.d("wang3", "$indexToFill and  $totalToFill")
            rawStory.fillPlaceholder(indexToFill, inputWord)  //replaces placeholder with inputWord
            indexToFill++
            updatePlaceHolderCount()
            findViewById<EditText>(R.id.input_word).setText("")
        }

        // if all place holders are filled, start a new activity
        if(indexToFill == totalToFill){
            Log.d("wang5", "$indexToFill and  $totalToFill")
            val showStoryIntent = Intent(this, ShowStoryActivity::class.java)
            showStoryIntent.putExtra("story", rawStory)
            startActivity(showStoryIntent) //jumpy to stowStoryIntent.kt
        }
        else{
            Log.d("wang4", "$indexToFill and  $totalToFill")
            findViewById<EditText>(R.id.input_word).setHint("${rawStory.placeholders.get(indexToFill)}")  //show hint for next placeholder; if it is empty for inputWord then it will show first placeholder
        }
    }

    // helper, update the number of placeholders to be filled
    fun updatePlaceHolderCount(){
        val leftToFill = totalToFill - indexToFill
        Log.d("wang1", "$indexToFill and  $totalToFill")
        findViewById<TextView>(R.id.words_left).setText("$leftToFill times of input word(s) left");  //from activity_fill_in_words.xml
    }

    // finish the activity when restart from show_story_activity
    override fun onRestart() {
        super.onRestart()
        finish()
    }
}
