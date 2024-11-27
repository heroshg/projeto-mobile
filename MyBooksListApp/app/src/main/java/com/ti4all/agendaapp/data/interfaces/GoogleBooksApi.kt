package com.ti4all.agendaapp.data.interfaces

import retrofit2.http.GET
import retrofit2.http.Query
import com.ti4all.agendaapp.models.BooksResponse

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
    ): BooksResponse
}