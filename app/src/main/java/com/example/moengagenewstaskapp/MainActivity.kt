package com.example.moengagenewstaskapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moengagenewstaskapp.adpter.NewsAdapter
import com.example.moengagenewstaskapp.firebaseUtils.firebaseMessaging
import com.example.moengagenewstaskapp.model.News
import com.example.moengagenewstaskapp.networkUtils.fetchNewsData
import com.example.moengagenewstaskapp.networkUtils.parseNewsJson
import com.example.moengagenewstaskapp.utils.handleNotifications
import com.example.moengagenewstaskapp.utils.permissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsRecyclerView: RecyclerView
    private var newsList: List<News> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //for asking permission for android 13+ devices
        permissionManager(this, this)
        //for getting firebase token
        firebaseMessaging()
        // for handling payload notifications
        handleNotifications(intent)


        // Initialize RecyclerView
        newsRecyclerView = findViewById(R.id.newsRecyclerView)
        if(:: newsRecyclerView.isInitialized){
            newsRecyclerView.layoutManager = LinearLayoutManager(this)
        }

        // Fetch news data
        fetchNews()
    }

    // menu items for sorting
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_old_to_new -> {
                sortArticles(oldToNew = true)
                true
            }
            R.id.action_sort_new_to_old -> {
                sortArticles(oldToNew = false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //for sorting articles
    private fun sortArticles(oldToNew: Boolean) {
        val sortedList = if (oldToNew) {
            newsList.sortedBy { parseDate(it.publishedAt) }
        } else {
            newsList.sortedByDescending { parseDate(it.publishedAt) }
        }
        newsAdapter.updateNewsList(sortedList)
    }

    //for comparing published date with current date
    private fun parseDate(dateString: String?): Date? {
        return try {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputDateFormat.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun fetchNews() {
        // Fetch news data from the API
        CoroutineScope(Dispatchers.IO).launch {
            val response = fetchNewsData()
            if (response != null) {
                // Parse the JSON response
                val newsList = parseNewsJson(response)
                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    // Update the UI with the fetched news data
                    newsAdapter = NewsAdapter(this@MainActivity, newsList)
                    newsRecyclerView.adapter = newsAdapter
                }
            }
        }
    }
}