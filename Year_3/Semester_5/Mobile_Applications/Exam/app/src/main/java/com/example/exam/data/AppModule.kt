package com.example.exam.data

import android.content.Context
import androidx.room.Room
import com.example.exam.ConnectivityRepository
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
    private const val API_URL = "http://10.0.2.2:2407"

    @Provides
    @Singleton
    fun provideTemplateDatabase(@ApplicationContext context: Context): TemplateDatabase =
        Room.databaseBuilder(
            context,
            TemplateDatabase::class.java,
            TemplateDatabase.name
        ).build()

    @Provides
    @Singleton
    fun provideTemplateRepository(database: TemplateDatabase): TemplateRepository =
        TemplateRepositoryImpl(dao = database.dao)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTemplateService(retrofit: Retrofit): TemplateService =
        retrofit.create(TemplateService::class.java)

    @Provides
    @Singleton
    fun provideConnectivityRepository(@ApplicationContext context: Context): ConnectivityRepository =
        ConnectivityRepository(context)
}