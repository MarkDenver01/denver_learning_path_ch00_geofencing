package com.example.denver_learning_path_ch00_geofencing.domain.datasource

import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.entity.GeofenceEntity
import kotlinx.coroutines.flow.Flow

interface Geofence {

    suspend fun addGeofence(geofenceEntity: GeofenceEntity)

    suspend fun removeGeofence(geofenceEntity: GeofenceEntity)
}