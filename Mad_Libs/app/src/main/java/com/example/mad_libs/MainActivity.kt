package com.example.mad_libs
/*
 * This app asks you to fill the words in a story
 * Note: Min SDK version for this app is 21
 * Implemented Feature:
 * 1) Main activity
 * 2) Fill in words activity
 * 3) Show story activity
 * 4) Text to speech when show_story_activity starts
 * 5) Separate layout is defined for landscape
 * 6) Status maintained when screen is rotated
 * 7) Show the number of placeholders left to fill
 */

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    val storyList: MutableList<String> = ArrayList<String>()

    // set spinner adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storyList.addAll(listOf("wannabe", "clothing", "dance", "marty", "tarzan", "university"))
        //use another xml file(spinner_item.xml) for spinner content; spinner from activity_mail.xml
        //didn't use string.xml for a spinner content as in PuppyPower app
        val spinnerAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, R.id.item, storyList)
        findViewById<Spinner>(R.id.story_spinner).adapter = spinnerAdapter  //R.id.story_spinner from activity_main.xml
    }

    // start fill words activity; "GET STARTED" Onclick button from activity_main.xml
    //jump to FillInWordsActivity
    fun getStarted(view: View){
        val showWordsIntent = Intent(this, FillInWordsActivity::class.java)

        //find corresponding storyList value by using spinner's selected item's index; send with Intent
        showWordsIntent.putExtra("storyName",
            storyList[findViewById<Spinner>(R.id.story_spinner).selectedItemPosition]
        )
        startActivity(showWordsIntent)
    }

}
