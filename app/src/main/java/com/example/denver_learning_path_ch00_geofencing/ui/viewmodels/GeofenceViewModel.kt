package com.example.denver_learning_path_ch00_geofencing.ui.viewmodels

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.entity.GeofenceEntity
import com.example.denver_learning_path_ch00_geofencing.data.repository.DataStoreRepository
import com.example.denver_learning_path_ch00_geofencing.data.repository.DatabaseRepository
import com.example.denver_learning_path_ch00_geofencing.receivers.GeofenceBroadcastReceiver
import com.example.denver_learning_path_ch00_geofencing.utils.Permissions
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeofenceViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val app = application

    private var geofencingClient =
        LocationServices.getGeofencingClient(application.applicationContext)

    var geoId: Long = 0L
    var geoName: String = "Default"
    var geoCountryCode: String = ""
    var geoLocationName: String = "Search a City"
    var geoLatLng: LatLng = LatLng(0.0, 0.0)
    var geoRadius: Float = 500f
    var geoSnapShot: Bitmap? = null

    var geoCitySelected = false
    var geofenceReady = false
    var geofencePrepared = false

    fun resetSharedValues() {
        geoId = 0L
        geoName = "Default"
        geoCountryCode = ""
        geoLocationName = "Search a City"
        geoLatLng = LatLng(0.0, 0.0)
        geoRadius = 500f
        geoSnapShot = null

        geoCitySelected = false
        geofenceReady = false
        geofencePrepared = false
    }

    val readFirstLaunchAsLiveData = dataStoreRepository.readFirstLaunch.asLiveData()
    val readGeofenceAsLiveData = databaseRepository.readGeofence.asLiveData()

    fun saveFirstLaunch(firstLaunch: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveFirstLaunch(firstLaunch)
        }

    fun addGeofence(geofenceEntity: GeofenceEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.addGeofence(geofenceEntity)
        }

    fun removeGeofence(geofenceEntity: GeofenceEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.removeGeofence(geofenceEntity)
        }

    fun addGeofenceToDatabase(location: LatLng) {
        val geofenceEntity =
            GeofenceEntity(
                geoId,
                geoName,
                geoLocationName,
                location.latitude,
                location.longitude,
                geoRadius,
                geoSnapShot!!
            )
        addGeofence(geofenceEntity)
    }

    private fun setPendingIntent(geoId: Int): PendingIntent {
        val intent = Intent(app, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            app,
            geoId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun startGeofence(
        latitude: Double,
        longitude: Double
    ) {
        if (Permissions.hasBackgroundLocationPermission(app)) {
            val geofence = Geofence.Builder()
                .setRequestId(geoId.toString())
                .setCircularRegion(
                    latitude,
                    longitude,
                    geoRadius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(
                    Geofence.GEOFENCE_TRANSITION_ENTER
                            or Geofence.GEOFENCE_TRANSITION_EXIT
                            or Geofence.GEOFENCE_TRANSITION_DWELL
                )
                .setLoiteringDelay(5000)
                .build()

            val geofenceRequest = GeofencingRequest.Builder()
                .setInitialTrigger(
                    GeofencingRequest.INITIAL_TRIGGER_ENTER
                            or GeofencingRequest.INITIAL_TRIGGER_EXIT
                            or GeofencingRequest.INITIAL_TRIGGER_DWELL
                )
                .addGeofence(geofence)
                .build()


            // TODO revise permission code here
            geofencingClient.addGeofences(geofenceRequest, setPendingIntent(geoId.toInt())).run {
                addOnSuccessListener {
                    Log.d("geofence", "successfully added.")
                }
                addOnFailureListener {
                    Log.e("geofence", it.message.toString())
                }
            }
        } else {
            Log.d("geofence", "permission not granted")
        }
    }


}