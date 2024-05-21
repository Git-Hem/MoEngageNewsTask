package com.example.moengagenewstaskapp.networkUtils

import com.example.moengagenewstaskapp.model.News
import com.example.moengagenewstaskapp.model.NewsSource
import org.json.JSONObject

fun parseNewsJson(jsonString: String): List<News> {
    // Parse the JSON string into a list of News objects
    val newsList = mutableListOf<News>()
    val jsonObject = JSONObject(jsonString)
    val jsonArray = jsonObject.getJSONArray("articles")


    // Iterate over each news object in the JSON array
    for (i in 0 until jsonArray.length()) {
        // Get the current news object
        val newsObject = jsonArray.getJSONObject(i)

        // Get the source object from the news object
        val sourceObject = newsObject.getJSONObject("source")

        // Create a NewsSource object from the source object
        val source = NewsSource(
            id = sourceObject.getString("id"),
            name = sourceObject.getString("name")
        )

        // Get the author, title, description, url, urlToImage, publishedAt, and content from the news object
        val author = newsObject.optString("author")
        val title = newsObject.getString("title")
        val description = newsObject.optString("description")
        val url = newsObject.getString("url")
        val urlToImage = newsObject.optString("urlToImage")
        val publishedAt = newsObject.getString("publishedAt")
        val content = newsObject.optString("content")

        // Create a News object and add it to the list
        val news = News(
            source = source,
            author = author,
            title = title,
            description = description,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )
        // Add the News object to the list
        newsList.add(news)
    }

    return newsList
}