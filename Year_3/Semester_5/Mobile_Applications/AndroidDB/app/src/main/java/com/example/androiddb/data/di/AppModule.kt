package com.example.androiddb.data.di

import android.content.Context
import androidx.room.Room
import com.example.androiddb.data.local.DailyReportDatabase
import com.example.androiddb.data.repository.DailyReportRepositoryImpl
import com.example.androiddb.domain.repository.DailyReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
}