package com.example.androiddbserver.data.network

import com.example.androiddbserver.domain.model.DailyReport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReportsService {
    @GET("/reports")
    fun getReports(): Call<List<DailyReport>>

    @GET("/reports/{id}")
    fun getReportById(@Path("id") id: Int): Call<DailyReport>

    @POST("/reports")
    fun addReport(@Body report: DailyReport): Call<DailyReport>

    @PUT("/reports/{id}")
    fun updateReport(@Path("id") id: Int, @Body report: DailyReport): Call<DailyReport>

    @DELETE("/reports/{id}")
    fun deleteReport(@Path("id") id: Int): Call<DailyReport>
}