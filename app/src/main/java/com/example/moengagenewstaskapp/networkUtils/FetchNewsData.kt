package com.example.moengagenewstaskapp.networkUtils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun fetchNewsData(): String? {
    val apiUrl = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"

    var response: String? = null

    try {
        // Create a URL object
        val url = URL(apiUrl)
        val urlConnection = url.openConnection() as HttpURLConnection

        try {
            // Read the response
            val inputStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = StringBuilder()
            var line: String?
            while (inputStream.readLine().also { line = it } != null) {
                result.append(line)
            }
            response = result.toString()
        } finally {
            urlConnection.disconnect()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return response
}