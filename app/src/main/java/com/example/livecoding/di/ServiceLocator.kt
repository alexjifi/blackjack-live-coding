package com.example.livecoding.di

import com.example.livecoding.data.remote.CardGameApi
import com.example.livecoding.data.remote.CardGameRemoteDataSource
import com.example.livecoding.data.repository.CardGameRepositoryImpl
import com.example.livecoding.domain.repository.CardGameRepository
import com.example.livecoding.domain.usecase.DrawOpeningHandUseCase
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {

    lateinit var activityContext: Context
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: CardGameApi by lazy {
        retrofit.create(CardGameApi::class.java)
    }

    private val remoteDataSource: CardGameRemoteDataSource by lazy {
        CardGameRemoteDataSource(api)
    }

    private val repository: CardGameRepository by lazy {
        CardGameRepositoryImpl(remoteDataSource)
    }

    fun provideDrawOpeningHandUseCase(): DrawOpeningHandUseCase {
        return DrawOpeningHandUseCase(repository)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private const val BASE_URL = "https://deckofcardsapi.com/"
}
