package com.example.itemizer

import android.content.Context
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.util.*

class PropertyManager {

    companion object {

        // Static Methods
        fun getProperty(key : String, context: Context): String {

            var propStr: String = "";

            try {
                val properties: Properties = Properties();
                val assetManager: AssetManager = context.getAssets()
                val inputStream: InputStream = assetManager.open("app.properties");
                properties.load(inputStream);
                propStr = properties.getProperty(key);
            } catch (e: IOException) {
                e.fillInStackTrace();
            } finally {
                return propStr
            }
        }
    }
}