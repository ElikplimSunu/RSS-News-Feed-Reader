package com.sunueric.newsrssfeedapp.data.network.models


data class Item(
    var title: String? = null,
    var articleLink: String? = null,
    var description: String? = null,
    var publicationDate: String? = null,
    var imageUrl: String? = null
)