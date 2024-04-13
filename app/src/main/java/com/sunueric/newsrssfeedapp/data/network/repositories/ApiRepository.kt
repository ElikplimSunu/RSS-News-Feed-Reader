package com.sunueric.newsrssfeedapp.data.network.repositories

import android.util.Log
import com.sunueric.newsrssfeedapp.data.network.RssService
import com.sunueric.newsrssfeedapp.data.network.models.Item
import com.sunueric.newsrssfeedapp.utils.parseRssFeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ApiRepository @Inject constructor(private val rssService: RssService) {
    private val _newsItems = MutableStateFlow<List<Item>>(emptyList())
    val newsItems: StateFlow<List<Item>?> = _newsItems.asStateFlow()

    suspend fun fetchRssFeed(): List<Item> {
        try {
            val response = rssService.fetchRssFeedAsString()
            val feedString = response.string() // Get the raw XML string
            val newsItems = parseRssFeed(feedString) // Parse it
            _newsItems.value = newsItems // Update the state flow
            return newsItems
        } catch (e: Exception) {
            Log.e("RSS Fetch Error", "Failed to fetch RSS feed: $e", e)
            return emptyList()
        }
    }
}

