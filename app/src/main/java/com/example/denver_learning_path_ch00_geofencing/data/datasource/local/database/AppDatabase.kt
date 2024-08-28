package com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.dao.GeofenceDao
import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.entity.GeofenceEntity
import com.example.denver_learning_path_ch00_geofencing.utils.Converters

@Database(
    entities = [GeofenceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun geofenceDao(): GeofenceDao
}