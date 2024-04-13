package com.sunueric.newsrssfeedapp.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET

interface RssService {
    @GET("Arts.xml")
    suspend fun fetchRssFeedAsString(): ResponseBody // Retrofit's ResponseBody
}