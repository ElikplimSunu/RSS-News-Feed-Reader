package com.sunueric.newsrssfeedapp.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sunueric.newsrssfeedapp.ui.theme.NewsRSSFeedAppTheme
import com.sunueric.newsrssfeedapp.viewmodels.NewsItemViewModel

@Composable
fun NewsFeed() {
    val newsItemViewModel: NewsItemViewModel = hiltViewModel()

    val articles by newsItemViewModel.newsItems.collectAsState()

    // Fetch RSS feed
    LaunchedEffect(key1 = Unit) {
        newsItemViewModel.fetchRssFeed()
    }

    articles?.let {
        NewsItem("https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png", "Arts", it)
    } ?: run {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.25f)
            )
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = UI_MODE_TYPE_NORMAL or UI_MODE_NIGHT_YES,
    device = Devices.TV_720p)
@Composable
fun PreviewNewsFeed() {
    NewsRSSFeedAppTheme {
        NewsItem("random", "Arts", emptyList())
    }
}