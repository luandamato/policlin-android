package br.com.policlinsaude.core.helper

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import com.google.android.gms.location.LocationRequest
import io.reactivex.Single
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.concurrent.TimeUnit


/**
 * Created by lmiyagi on 3/23/18.
 */
object LocationHelper {

    const val DEFAULT_STATIC_MAP_HEIGHT = 150
    const val DEFAULT_STATIC_MAP_WIDTH = 600
    const val DEFAULT_STATIC_ZOOM = 0.1

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): Single<PresentationLocation> {
    /*    if (!isGPSEnabled(context)) {
            return Single.error(LocationNotEnabledException())
        }*/

        val request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(10)
                .setNumUpdates(1)
                .setInterval(1000)

        val latitude =  "-23.1963833".toDouble()
        val longitude = "-45.8959785".toDouble()


        val locationProvider = ReactiveLocationProvider(context)
        return locationProvider.getUpdatedLocation(request)
                .timeout(10, TimeUnit.SECONDS)
                .firstOrError()
                .map {
                    PresentationLocation(it.latitude, it.longitude)
                   // PresentationLocation(latitude, longitude)
                }


    }

    private fun isGPSEnabled(context: Context): Boolean {
        return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun provideStaticMapUrl(latitude: Double, longitude: Double): String {
        val url = "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=$DEFAULT_STATIC_ZOOM&scale=1" +
                "&size=${DEFAULT_STATIC_MAP_WIDTH}x$DEFAULT_STATIC_MAP_HEIGHT" +
                "&maptype=roadmap&format=jpg&visual_refresh=true" +
                "&markers=size:mid%7Ccolor:0xff0000%7Clabel:1%7C$latitude,$longitude"
        return url
    }

    class LocationNotEnabledException : Exception()
}