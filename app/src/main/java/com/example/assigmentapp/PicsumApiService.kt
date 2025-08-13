package com.example.assigmentapp // Your package name

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApiService {
    @GET("v2/list") // Endpoint for listing images
    suspend fun getImages(
        @Query("page") page: Int = 1, // You can make page dynamic if needed
        @Query("limit") limit: Int = 10
    ): Response<List<PicsumImage>> // Expecting a List of PicsumImage objects
}