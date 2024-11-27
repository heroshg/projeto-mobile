package com.ti4all.agendaapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.ti4all.agendaapp.data.interfaces.GoogleBooksApi

object RetrofitInstance {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GoogleBooksApi by lazy {
        retrofit.create(GoogleBooksApi::class.java)
    }
}