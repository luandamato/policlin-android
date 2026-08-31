package br.com.policlinsaude.util.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import br.com.policlinsaude.data.models.PresentationLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Helper de localização.
 * Migrado/adaptado de `_legacy/.../core/helper/LocationHelper.kt` (era RxJava; agora suspende).
 */
object LocationHelper {

    const val DEFAULT_STATIC_MAP_HEIGHT = 150
    const val DEFAULT_STATIC_MAP_WIDTH = 600
    const val DEFAULT_STATIC_ZOOM = 0.1

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): PresentationLocation {
        if (!hasLocationPermission(context)) {
            throw LocationNotEnabledException("Permissão de localização não concedida")
        }

        val client = LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { continuation ->
            client.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(PresentationLocation(location.latitude, location.longitude))
                    } else {
                        continuation.resumeWithException(LocationNotEnabledException())
                    }
                }
                .addOnFailureListener { error ->
                    if (continuation.isActive) continuation.resumeWithException(error)
                }
        }
    }

    fun isGPSEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun hasLocationPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun provideStaticMapUrl(latitude: Double, longitude: Double): String {
        return "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=$DEFAULT_STATIC_ZOOM&scale=1" +
            "&size=${DEFAULT_STATIC_MAP_WIDTH}x$DEFAULT_STATIC_MAP_HEIGHT" +
            "&maptype=roadmap&format=jpg&visual_refresh=true" +
            "&markers=size:mid%7Ccolor:0xff0000%7Clabel:1%7C$latitude,$longitude"
    }

    class LocationNotEnabledException(message: String = "Localização não disponível") : Exception(message)
}