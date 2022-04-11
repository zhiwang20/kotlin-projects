package com.example.mad_libs
/*
 * *** NOTE: YOU MAY NEED TO MODIFY THIS FILE BY PUTTING IT INTO A 'package'. ***
 *
 * This class represents a Mad Libs story that comes from a text file.
 * You can construct it and pass an input stream or Scanner to read the story text.
 *
 * You can get the story's text by calling its toString method.
 * A Story is Serializable, so it can be packed into an Intent as "extra" data.
 */

import android.content.Context
import androidx.annotation.RawRes
import java.io.*
import java.util.*

//Serializable is going to convert an object to byte stream.
//So the user can pass the data between one activity to another activity
class Story : Serializable {
    // text of the story
    private var text = ""

    // list of placeholders to fill in; take a look at files in raw folder
    private var _placeholders : MutableList<Placeholder> = mutableListOf()

    /**
     * A small class to represent a placeholder such as "<proper noun>" and an index in the
     * original text at which it occurs.
    </proper> */
    private class Placeholder(var text: String, val index: Int) : Serializable {
        var replacement: String = ""

        /** Returns a text representation of the placeholder for debugging. */
        //toString: Returns a string representation of the object
        override fun toString(): String {
            //index here is the position for left tag: <; text here is hint word: adjective for <adjective>
            return "{text=$text, replacement=$replacement, index=$index}"
        }
    }

    /**
     * sets whether this story will be outputted in HTML mode with B tags around placeholders
     * (default false)
    */
    var isHtmlMode: Boolean = false      // set true to surround placeholders with <b></b> tags


    // properties
    /** returns a read-only list of placeholders in the story
     * gets hint word text from a format of "{text=$text, replacement=$replacement, index=$index}" based on index of List<String> of placeholders: 0,1,2,...*/
    val placeholders: List<String>
        get() = this._placeholders.map { it -> it.text }

    /** returns total number of placeholders in the story */
    val placeholderCount: Int
        get() = _placeholders.size


    /** sets the placeholder at the given index to be replaced by the given text
     * _placeholders is a MutableList of Placeholder; Placeholder.replacement replaces "" with replacementText; index starts from 0,1,2,...; don't confuse with indexOfHintWordLeftTag
     * so each string in _placeholders has a format of "hintWordOfPlaceHolder, replacementText, indexOfHintWordLeftTag" */
    fun fillPlaceholder(index: Int, replacementText: String) {
        _placeholders[index].replacement = replacementText
    }

    /** reads initial story text from the given Activity context and raw resource ID. */
    fun read(context: Context, @RawRes resourceID: Int) {
        read(context.resources.openRawResource(resourceID))
    }

    /** reads initial story text from the given Activity context and raw resource name.
     * This is used in FillInWordsActivity */
    fun read(context: Context, resource: String) {
        val fileID = context.resources.getIdentifier(resource, "raw", context.packageName)   // R.raw.clothing
        read(context.resources.openRawResource(fileID))   //read inputStream
    }

    /** reads initial story text from the given input stream */
    fun read(stream: InputStream) {
        read(Scanner(stream))    //read Scanner
    }

    /** reads initial story text from the given Reader */
    fun read(input: Reader) {
        read(Scanner(input))
    }

    /** reads initial story text from the given Scanner
     *  _placeholders finishes storing */
    fun read(input: Scanner) {
        // read data from input source
        val sb = StringBuilder()
        while (input.hasNextLine()) {
            sb.append(input.nextLine())
            sb.append('\n')
        }

        //Return Value: This method always returns a string representing the data contained by StringBuilder Object.
        // didn't use override toString() function from the bottom; not the one inside PlaceHolder class
        this.text = sb.toString()

        // find _placeholders in input; each placeholder inside <> tag
        //indexOf(String str) : Returns the index(int) within this string of the first occurrence of the specified substring.
        var lt = text.indexOf('<')
        var gt = text.indexOf('>')
        //alternative use: lt in 0..(gt - 1)
        while (lt in 0 until gt) {
            val phText = text.substring(lt + 1, gt)    //example <plural noun> --> plural noun
            val ph = Placeholder(phText, lt)   //each ph has a format of {text=$text, replacement=$replacement, index=$index}; index is the position for <
            _placeholders.add(ph)

            lt = text.indexOf('<', gt + 1)  //locate next placeholder starting from gt + 1
            if (lt < 0) break
            gt = text.indexOf('>', lt + 1)
        }
    }

    /*
    /** Returns "an" if the the placeholder at the given index begins with a vowel (AEIOU), else "a". */
    fun aVsAn(index: Int): String {
        return aVsAn(_placeholders[index].text)
    }

    /** Returns "an" if the given string begins with a vowel (AEIOU), else "a". */
    fun aVsAn(s: String): String {
        return if (startsWithVowel(s)) "an" else "a"
    }

    /** Returns true if the placeholder at the given index begins with a vowel. */
    fun startsWithVowel(index: Int): Boolean {
        return startsWithVowel(_placeholders[index].text)
    }

    /** Returns true if the given string begins with a vowel (AEIOU). */
    fun startsWithVowel(s: String): Boolean {
        return !s.isEmpty() && "aeiou".indexOf(s.trim().toLowerCase()[0]) >= 0
         //s.trim().toLowerCase()[0] is the first character of s; indexOf returns -1 if no such character occur
    }
    */

    /** Returns story text, including any _placeholders filled in.
     * used in showStoryActivity.kt */
    override fun toString(): String {
        var storyText = text

        // fill in the _placeholders
        // (go backwards so that the indexes don't shift under our feet)
        for (i in _placeholders.indices.reversed()) {
            val ph = _placeholders[i]    //each string in _placeholders has a format of "{text=$text, replacement=$replacement, index=$index}"

            var repl = ph.replacement  //used only if in HTmlMode
            if (isHtmlMode) {
                repl = "<b>$repl</b>"
            }

            //substring(int beginIndex, int endIndex) : Returns a string that is a substring of this string; endIndex is exclusive
            val before = storyText.substring(0, ph.index) //substring that is before of the placeholder
            val after = storyText.substring(ph.index + ph.text.length + 2)   // +2 because of <>
            storyText = before + repl + after
        }
        return storyText
    }
}