package com.example.exam.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TemplateService {
    @GET("/listings")
    fun getListings(): Call<List<Listing>>

    @POST("/property")
    fun addListing(@Body listing: Listing): Call<Listing>

    @GET("/property/{id}")
    fun getListing(@Path("id") id: Int): Call<Listing>

    @GET("/types")
    fun getTypes(): Call<List<String>>

    @PUT("/register/{type}")
    fun registerType(@Path("type") type: String): Call<Listing>

    @GET("/interest")
    fun getInterest(): Call<List<Listing>>
}