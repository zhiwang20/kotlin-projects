package com.example.mad_libs

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ShowStoryActivity : AppCompatActivity() {

    lateinit var tts: TextToSpeech

    // Display the story and initialize text to speech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_story)
        val story = intent.getSerializableExtra("story")  //get story from FillInWordsActivity

        if(story != null){
            //toString() function from bottom page of Story class: finally starts to replace placeholder and return final version of story
            findViewById<TextView>(R.id.my_story).text = story.toString()    //TextView from activity_show_story.xml
            findViewById<TextView>(R.id.my_story).movementMethod = ScrollingMovementMethod()
            tts = TextToSpeech(this, TextToSpeech.OnInitListener {
                Log.d("output", "ttsReady")
                tts.speak(story.toString(), TextToSpeech.QUEUE_FLUSH, null, null)   //story.toString()
            })
        }
    }

    // on click button to stop the activity; Onclick button "Make another story"; jumpy to main menu by stopping all intents
    fun makeAnotherStory(view: View){
        finish()
    }
}
