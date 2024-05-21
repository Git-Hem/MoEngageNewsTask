package com.example.moengagenewstaskapp.adpter


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moengagenewstaskapp.R
import com.example.moengagenewstaskapp.model.News
import com.example.moengagenewstaskapp.utils.formatDateString
import java.net.HttpURLConnection
import java.net.URL

class NewsAdapter(private val context: Context, private var newsList: List<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define the views in the layout
        val title: TextView = view.findViewById(R.id.news_title)
        val author: TextView = view.findViewById(R.id.news_author)
        val description: TextView = view.findViewById(R.id.news_description)
        val url: TextView = view.findViewById(R.id.news_url)
        val created : TextView = view.findViewById(R.id.news_published)
        val image: ImageView = view.findViewById(R.id.news_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // Bind the data to the views in the view holder
        val news = newsList[position]
        holder.title.text = news.title
        holder.author.text = "By ${news.author?: "Unknown Author"}"
        holder.description.text = news.description?: "No Description"

        // Format the date
        val formattedDate = news.publishedAt?.let {
            formatDateString(it, "yyyy-MM-dd'T'HH:mm:ss'Z'", "dd/MM/yyyy")
        } ?: "Unknown Date"
        holder.created.text = " Published on ${formattedDate}"

        // Load the image using a custom AsyncTask
        if (news.urlToImage != null) {
            // If the image URL is not null, show the ImageView
            holder.image.visibility = View.VISIBLE
            DownloadImageTask(holder.image).execute(news.urlToImage)
        } else {
            // If the image URL is null, hide the ImageView
            holder.image.visibility = View.GONE
        }


        holder.url.setOnClickListener {
            if (news.url.isNotEmpty()){
                val context = holder.itemView.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = newsList.size
    fun updateNewsList(newList: List<News>) {
        newsList = newList
        notifyDataSetChanged()
    }
    // Add a new method to load images asynchronously
    private class DownloadImageTask(val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urlToImage = urls[0]
            var bitmap: Bitmap? = null
            try {
                // Load the image from the URL
                val url = URL(urlToImage)
                // Open the connection
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                // Get the input stream and decode the image
                val inputStream = connection.inputStream
                // Decode the image
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                // Display the image in the ImageView
                imageView.setImageBitmap(result)
            }
        }
    }
}

