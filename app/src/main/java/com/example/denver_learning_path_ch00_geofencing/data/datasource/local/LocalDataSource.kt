package com.example.denver_learning_path_ch00_geofencing.data.datasource.local

import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.dao.GeofenceDao
import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.entity.GeofenceEntity
import com.example.denver_learning_path_ch00_geofencing.domain.datasource.Geofence
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class LocalDataSource @Inject constructor(
    private val geofenceDao: GeofenceDao
) : Geofence {

    override suspend fun readGeofence(): Flow<MutableList<GeofenceEntity>> =
        geofenceDao.readGeofence()


    override suspend fun addGeofence(geofenceEntity: GeofenceEntity) {
        geofenceDao.addGeofence(geofenceEntity)
    }

    override suspend fun removeGeofence(geofenceEntity: GeofenceEntity) {
        geofenceDao.removeGeofence(geofenceEntity)
    }
}