package com.sunueric.newsrssfeedapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunueric.newsrssfeedapp.data.network.models.Item
import com.sunueric.newsrssfeedapp.data.network.repositories.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsItemViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): ViewModel() {
    private val _newsItems = MutableStateFlow<List<Item>?>(null)
    val newsItems = _newsItems.asStateFlow()

    suspend fun fetchRssFeed() {
        viewModelScope.launch {
            _newsItems.value = apiRepository.fetchRssFeed()
        }
    }
}