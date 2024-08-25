package com.example.denver_learning_path_ch00_geofencing.di

import android.content.Context
import androidx.room.Room
import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.AppDatabase
import com.example.denver_learning_path_ch00_geofencing.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideGeofenceDao(database: AppDatabase) = database.geofenceDao()
}