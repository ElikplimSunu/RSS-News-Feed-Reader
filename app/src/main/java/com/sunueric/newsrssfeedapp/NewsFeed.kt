package com.sunueric.newsrssfeedapp


import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET

interface RssService {
    @GET("Arts.xml")
    suspend fun fetchRssFeedAsString(): ResponseBody // Retrofit's ResponseBody
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://rss.nytimes.com/services/xml/rss/nyt/")
            .build() // Removed the XML converter
    }

    val rssService: RssService by lazy {
        retrofit.create(RssService::class.java)
    }
}


