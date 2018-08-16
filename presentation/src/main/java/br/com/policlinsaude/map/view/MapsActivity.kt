package br.com.policlinsaude.map.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.LocationHelper
import br.com.policlinsaude.map.presenter.MapsPresenter
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.PresentationEstablishmentLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject


/**
 * Created by lmiyagi on 05/04/18.
 */
class MapsActivity : BaseActivity(), MapsView, OnMapReadyCallback {

    companion object {

        private const val EXTRA_ESTABLISHMENTS = "EXTRA_ESTABLISHMENTS"
        private const val EXTRA_IS_MEDICAL_GUIDE = "EXTRA_IS_MEDICAL_GUIDE"

        fun start(activity: Activity, establishments: ArrayList<PresentationEstablishmentLocation>, isMedicalGuide: Boolean) {
            val intent = Intent(activity, MapsActivity::class.java)
            intent.putParcelableArrayListExtra(EXTRA_ESTABLISHMENTS, establishments)
            intent.putExtra(EXTRA_IS_MEDICAL_GUIDE, isMedicalGuide)
            Log.d("PRESENTATION","MapsActivity - dentro de MapsActivity")
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: MapsPresenter

    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setupToolbar()
        val isMedicalGuide = intent.extras.getBoolean(EXTRA_IS_MEDICAL_GUIDE, false)
        setTitle(if (isMedicalGuide) R.string.title_medical_guide_map else R.string.title_own_network_map)

        (mapsFragment as SupportMapFragment).getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        presenter.onMapReady(intent.getParcelableArrayListExtra<PresentationEstablishmentLocation>(EXTRA_ESTABLISHMENTS))
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    override fun getCurrentLocation() {
//Andre
        val latitude =  "-23.1963833".toDouble()
        val longitude = "-45.8959785".toDouble()

        presenter.onLocationFetched( PresentationLocation(latitude, longitude))



       /* LocationHelper.getCurrentLocation(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            presenter.onLocationFetched(it)
                        },
                        onError = {
                            presenter.onLocationError(it)
                        })*/
   //Andre fim
    }

    @SuppressLint("MissingPermission")
    override fun setupMap(location: PresentationLocation, establishments: ArrayList<PresentationEstablishmentLocation>) {
        if (establishments.isNotEmpty()) {
            map?.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(LatLng(establishments[0].latitude, establishments[0].longitude), 10.0f))
        }
        establishments.forEach {
            if (it.latitude != 0.0 && it.longitude != 0.0) {
                map?.addMarker(MarkerOptions().position(
                        LatLng(it.latitude, it.longitude)).title(it.title))
            }
        }
        map?.isMyLocationEnabled = false //true //Andre
    }

    override fun showDialogError(error: Throwable) {
        showError(message = if (error is MessageErrorException) error.message!! else "")
    }

    override fun askForPermissions() {
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(object : BaseMultiplePermissionsListener() {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            report?.let {
                                if (it.areAllPermissionsGranted()) {
                                    presenter.onPermissionsGranted()
                                } else {
                                    presenter.onPermissionsDenied(it.isAnyPermissionPermanentlyDenied)
                                }
                            }
                        }
                    })
                    .check()
        } else {
            presenter.onPermissionsGranted()

        }*/
        presenter.onPermissionsGranted()
    }

    override fun showOnPermissionDeniedDialog(anyPermissionPermanentlyDenied: Boolean) {
        DialogHelper.showDialog(this,
                R.string.title_permissions_needed,
                R.string.text_permissions_needed,
                R.string.text_ok,
                listenerNegativeButton = { presenter.onPermissionsNeededDialogCanceled() },
                listenerPositiveButton = { presenter.onPermissionsNeededDialogOkClicked(anyPermissionPermanentlyDenied) },
                onDismiss = { presenter.onPermissionsNeededDialogOkClicked(anyPermissionPermanentlyDenied) })
    }

    override fun closeView() {
        finish()
    }

    override fun showLocationNotEnabledDialog() {
        DialogHelper.showDialog(this,
                R.string.title_error_oops,
                R.string.text_location_not_enabled,
                R.string.text_ok,
                R.string.action_cancel,
                { presenter.onLocationNotEnabledDialogOkClicked() },
                { presenter.onLocationNotEnabledDialogCancelClicked() },
                { presenter.onLocationNotEnabledDialogCancelClicked() })
    }

    override fun showLocationTimeoutDialog() {
        showDialogTryAgain({ askForPermissions() })
    }
}