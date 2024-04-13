package com.sunueric.newsrssfeedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sunueric.newsrssfeedapp.ui.screens.NewsFeed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidThreeTen.init(this)
            NewsFeed()
        }
    }
}

