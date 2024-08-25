package com.example.denver_learning_path_ch00_geofencing.data.datasource.local.database.entity

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.denver_learning_path_ch00_geofencing.utils.Constants
import com.example.denver_learning_path_ch00_geofencing.utils.Constants.DATABASE_TABLE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = DATABASE_TABLE)
@Parcelize
class GeofenceEntity(
    val geoId: Long,
    val name: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val snapShop: Bitmap
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}