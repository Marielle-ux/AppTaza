package com.example.apptaza.di

import com.example.apptaza.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Network {

    // Базовая настройка Gson (можно допиливать при необходимости)
    private val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setLenient()
        .create()

    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttp)
        .build()

    val api: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
