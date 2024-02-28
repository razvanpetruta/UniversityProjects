package com.example.androiddbserver.data.di

import android.content.Context
import androidx.room.Room
import com.example.androiddbserver.data.local.DailyReportDatabase
import com.example.androiddbserver.data.network.ConnectivityRepository
import com.example.androiddbserver.data.network.ReportsService
import com.example.androiddbserver.data.repository.DailyReportRepositoryImpl
import com.example.androiddbserver.domain.repository.DailyReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "http://10.0.2.2:3000"

    @Provides
    @Singleton
    fun provideDailyReportDatabase(@ApplicationContext context: Context): DailyReportDatabase =
        Room.databaseBuilder(
            context,
            DailyReportDatabase::class.java,
            DailyReportDatabase.name
        ).build()

    @Provides
    @Singleton
    fun provideDailyReportRepository(database: DailyReportDatabase): DailyReportRepository =
        DailyReportRepositoryImpl(dao = database.dao)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideReportsService(retrofit: Retrofit): ReportsService =
        retrofit.create(ReportsService::class.java)

    @Provides
    @Singleton
    fun provideConnectivityRepository(@ApplicationContext context: Context): ConnectivityRepository =
        ConnectivityRepository(context)
}