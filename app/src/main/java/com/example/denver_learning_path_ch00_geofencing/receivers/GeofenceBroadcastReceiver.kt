package com.example.denver_learning_path_ch00_geofencing.receivers

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.denver_learning_path_ch00_geofencing.R
import com.example.denver_learning_path_ch00_geofencing.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.denver_learning_path_ch00_geofencing.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.denver_learning_path_ch00_geofencing.utils.Constants.NOTIFICATION_ID
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)

                Log.e("geofence receiver: ", errorMessage)
            }

            when (geofencingEvent.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    Log.d("geofence receiver: ", "ENTER REGION")
                    displayNotification(context, "ENTER REGION")
                }

                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Log.d("geofence receiver: ", "EXIT REGION")
                    displayNotification(context, "EXIT REGION")
                }

                Geofence.GEOFENCE_TRANSITION_DWELL -> {
                    Log.d("geofence receiver: ", "DWELL")
                    displayNotification(context, "DWELL")
                }

                else -> {
                    Log.d("geofence receiver: ", "INVALID GEOFENCE TRANSITION")
                    displayNotification(context, "INVALID GEOFENCE TRANSITION")
                }
            }
        }
    }

    private fun displayNotification(context: Context, geofenceTransition: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Geofence")
            .setContentText(geofenceTransition)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}