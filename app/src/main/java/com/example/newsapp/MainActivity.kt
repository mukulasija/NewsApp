package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
       mAdapter=  NewsListAdapter(this)
        binding.recyclerview.adapter= mAdapter
    }
    private fun fetchData()
    {
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/science/in.json"
//        val url = "https://newsapi.org/v2/everything?q=tesla&from=2021-09-08&sortBy=publishedAt&apiKey=18ba28c7c40c45cdab79d36ce42908ac"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                 val newsArray = ArrayList<News>()
                 for(i in 0 until newsJsonArray.length())
                 {
                     val newsJsonObject = newsJsonArray.getJSONObject(i)
                     val news = News(
                      newsJsonObject.getString("title"),
                      newsJsonObject.getString("author"),
                      newsJsonObject.getString("url"),
                      newsJsonObject.getString("urlToImage")
                  )
                     newsArray.add(news)


                 }
                 mAdapter.updateNews(newsArray)


             },
            {
             }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            Response.Listener{
//             val newsJsonArray = it.getJSONArray("articles")
//                val newsArray = ArrayList<News>()
//                for(i in 0 until newsJsonArray.length()){
//                    val newsJsonObject = newsJsonArray.getJSONObject(i)
//                    val news = News(
//                        newsJsonObject.getString("title"),
//                        newsJsonObject.getString("author"),
//                        newsJsonObject.getString("url"),
//                        newsJsonObject.getString("urlToImage")
//                    )
//                    newsArray.add(news)
//                }
//                mAdapter.updateNews(newsArray)
//
//            },
//            {
//            }
//
//        )
//
//// Access the RequestQueue through your singleton class.
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

//        val list = ArrayList<String>()
//        for(i in 0 until 100){
//            list.add("item $i")
//        }
//        return list
    }


    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}