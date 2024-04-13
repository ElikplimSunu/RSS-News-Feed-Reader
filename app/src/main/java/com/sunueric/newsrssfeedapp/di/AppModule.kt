package com.sunueric.newsrssfeedapp.di

import com.sunueric.newsrssfeedapp.data.network.RssService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://rss.nytimes.com/services/xml/rss/nyt/")
        .build() // Removed the XML converter

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): RssService = retrofit
        .create(RssService::class.java)
}