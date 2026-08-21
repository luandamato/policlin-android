package br.com.policlinsaude.medicalGuideOptions.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.*
import br.com.policlinsaude.databinding.ActivityMedicalGuideOptionsBinding
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.domain.helper.InvalidData
import br.com.policlinsaude.medicalGuideOptions.presenter.MedicalGuideOptionsPresenter
import br.com.policlinsaude.model.*
import br.com.policlinsaude.qualificationInfo.QualificationFilterAdapter
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MedicalGuideOptionsActivity : BaseActivity(), MedicalGuideOptionsView {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MedicalGuideOptionsActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: MedicalGuideOptionsPresenter

    private lateinit var plans: MutableList<PresentationPlanOptions>
    private lateinit var cities: MutableList<PresentationCityOptions>
    private lateinit var specialities: MutableList<PresentationSpecialityServiceOptions>

    private var pressed: Boolean = false
    private var isLocationActive: Boolean = false

    private lateinit var professionalClass: MutableList<PresentationProfessionalClass>
    private lateinit var serviceType: MutableList<PresentationServiceType>
    private lateinit var establishmentType: MutableList<PresentationEstablishmentType>
    private lateinit var qualificationsForFilter: MutableList<PresentationQualificationForFilter>

    private var EMPTY = ""
    private var myPreferences = "myPrefs"
    private var PLAN = "plan"
    private var CITIES = "cities"
    private var SPECIALITY = "specialty"
    private var ADDRESS = "address"
    private var NEIGHBORHOOD = "neighborhood"
    private var ZIPCODE = "zipcode"
    private var PROFESSIONAL_CLASS = "professionalClass"
    private var NUMBER_ON_THE_BOARD = "number_on_the_board_filter"
    private var SERVICE_TYPE = "serviceType"
    private var PROF_FANTASY = "profFantasy"
    private var CNPJ = "CNPJ"
    private var PHONES = "phones"
    private var ESTABLISHMENT_TYPE = "establishment_type"

    private var CBA = "cbA"
    private var CBN = "cbN"
    private var CBP = "cbP"
    private var CBR = "cbR"
    private var CBE = "cbE"
    private var CBQ = "cbQ"
    private var CBG = "cbG"
    private var CBI = "cbI"
    private var CBD = "cbM"
    private var CBM = "cbN"

    private var zipcodeMask: TextWatcher? = null
    private var cnpjMask: TextWatcher? = null
    private var phoneMask: TextWatcher? = null

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var adapter: QualificationFilterAdapter

    private lateinit var binding: ActivityMedicalGuideOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(PLAN, 0)
        editor.putInt(CITIES, 0)
        editor.putInt(SPECIALITY, 0)
        editor.putInt(PROFESSIONAL_CLASS, 0)
        editor.putInt(SERVICE_TYPE, 0)
        editor.putInt(ESTABLISHMENT_TYPE, 0)
        editor.putInt(CBA, 0)
        editor.putInt(CBN, 0)
        editor.putInt(CBP, 0)
        editor.putInt(CBR, 0)
        editor.putInt(CBE, 0)
        editor.putInt(CBQ, 0)
        editor.putInt(CBG, 0)
        editor.putInt(CBI, 0)
        editor.putInt(CBD, 0)
        editor.putInt(CBM, 0)
        editor.putString(ADDRESS, EMPTY)
        editor.putString(NEIGHBORHOOD, EMPTY)
        editor.putString(ZIPCODE, EMPTY)
        editor.putString(NUMBER_ON_THE_BOARD, EMPTY)
        editor.putString(PROF_FANTASY, EMPTY)
        editor.putString(CNPJ, EMPTY)
        editor.putString(PHONES, EMPTY)
        editor.apply()

        binding = ActivityMedicalGuideOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)

        binding.viewFilters.infoTextViewLink.setOnClickListener {
            IntentHelper.openUrlInBrowser(this, getString(R.string.url_custom_infos))
        }

        binding.viewFilters.infoTextViewLink.setText(
            Html.fromHtml(getString(R.string.msg_information_about_icon_and_qualification_and_link)),
            TextView.BufferType.SPANNABLE
        )

        zipcodeMask = MascaraAndre.Mask.mask("#####-###", binding.viewFilters.zipcodeFilterTextView)
        cnpjMask = MascaraAndre.Mask.mask("##.###.###/####-##", binding.viewFilters.cnpjFilterTextView)
        binding.viewFilters.cnpjFilterTextView.addTextChangedListener(cnpjMask)

        phoneMask = MascaraAndre.Mask.mask("(##) ####-####", binding.viewFilters.phonesFilterTextView)
        binding.viewFilters.phonesFilterTextView.addTextChangedListener(phoneMask)

        showMedicalGuideOptions(PresentationMedicalGuideOptions(), null)
        presenter.getLocationPreference()

        setupToolbar()
        setOnClickListeners()
    }

    fun showSnackFeedback(message: String, isValid: Boolean, view: View) {
        val snackbar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val v: View = snackbar.view
        if (isValid)
            v.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        else
            v.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        snackbar.show()
    }

    override fun onStart() {
        super.onStart()
        binding.viewFilters.zipcodeFilterTextView.removeTextChangedListener(zipcodeMask)
        binding.viewFilters.zipcodeFilterTextView.setText(sharedPreferences.getString(ZIPCODE, EMPTY))

        binding.viewFilters.cnpjFilterTextView.removeTextChangedListener(cnpjMask)
        binding.viewFilters.cnpjFilterTextView.setText(sharedPreferences.getString(CNPJ, EMPTY))

        binding.viewFilters.phonesFilterTextView.removeTextChangedListener(phoneMask)
        binding.viewFilters.phonesFilterTextView.setText(sharedPreferences.getString(PHONES, EMPTY))

        presenter.getMedicalGuideOptions()
    }

    override fun askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
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
        }
    }

    private fun setOnClickListeners() {
        binding.buttonSearch.setOnClickListener {
            var maskValidation = true
            if (awesomeValidation.validate()) {
                if ((binding.viewFilters.cnpjFilterTextView.length() > 0) && (binding.viewFilters.cnpjFilterTextView.length() < 18)) {
                    showSnackFeedback("Campo CNPJ incompleto!", false, binding.viewFilters.cnpjFilterTextView)
                    maskValidation = false
                }
                if ((binding.viewFilters.zipcodeFilterTextView.length() > 0) && (binding.viewFilters.zipcodeFilterTextView.length() < 9)) {
                    showSnackFeedback("Campo CEP incompleto!", false, binding.viewFilters.cnpjFilterTextView)
                    maskValidation = false
                }
                if ((binding.viewFilters.phonesFilterTextView.length() > 0) && (binding.viewFilters.phonesFilterTextView.length() < 8)) {
                    showSnackFeedback("Campo Telefone incompleto!", false, binding.viewFilters.cnpjFilterTextView)
                    maskValidation = false
                }

                var numOptionsSelected = 0
                val editor = sharedPreferences.edit()

                val plan = plans[binding.spinnerPlan.selectedIndex]
                if (!(binding.spinnerPlan.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(PLAN, binding.spinnerPlan.selectedIndex)
                }

                val city = cities[binding.spinnerCity.selectedIndex]
                if (!(binding.spinnerCity.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(CITIES, binding.spinnerCity.selectedIndex)
                }

                val speciality = specialities[binding.spinnerSpeciality.selectedIndex]
                if (!(binding.spinnerSpeciality.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(SPECIALITY, binding.spinnerSpeciality.selectedIndex)
                }

                val professionalClass = professionalClass[binding.viewFilters.spinnerProfessionalClass.selectedIndex]
                if (!(binding.viewFilters.spinnerProfessionalClass.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(PROFESSIONAL_CLASS, binding.viewFilters.spinnerProfessionalClass.selectedIndex)
                }

                val serviceType = serviceType[binding.viewFilters.spinnerServiceType.selectedIndex]
                if (!(binding.viewFilters.spinnerServiceType.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(SERVICE_TYPE, binding.viewFilters.spinnerServiceType.selectedIndex)
                }

                val establishmentType = establishmentType[binding.viewFilters.spinnerEstablishmentType.selectedIndex]
                if (!(binding.viewFilters.spinnerEstablishmentType.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(ESTABLISHMENT_TYPE, binding.viewFilters.spinnerEstablishmentType.selectedIndex)
                }

                val address_filter = if (binding.viewFilters.addressplanFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.addressplanFilterTextView.text.toString()
                if (address_filter != "") {
                    numOptionsSelected++
                    editor.putString(ADDRESS, address_filter)
                }
                val neighborhood_filter = if (binding.viewFilters.neighborhoodFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.neighborhoodFilterTextView.text.toString()
                if (neighborhood_filter != "") {
                    numOptionsSelected++
                    editor.putString(NEIGHBORHOOD, neighborhood_filter)
                }
                val zipcode_filter = if (binding.viewFilters.zipcodeFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.zipcodeFilterTextView.text.toString()
                if (zipcode_filter != "") {
                    numOptionsSelected++
                    editor.putString(ZIPCODE, zipcode_filter)
                }
                val number_on_the_board_filter = if (binding.viewFilters.numberOnTheBoardFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.numberOnTheBoardFilterTextView.text.toString()
                if (number_on_the_board_filter != "") {
                    numOptionsSelected++
                    editor.putString(NUMBER_ON_THE_BOARD, number_on_the_board_filter)
                }
                val prof_fantasy_fliter = if (binding.viewFilters.profFantasyCompanyFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.profFantasyCompanyFilterTextView.text.toString()
                if (prof_fantasy_fliter != "") {
                    numOptionsSelected++
                    editor.putString(PROF_FANTASY, prof_fantasy_fliter)
                }
                val cnpj_filter = if (binding.viewFilters.cnpjFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.cnpjFilterTextView.text.toString()
                if (cnpj_filter != "") {
                    numOptionsSelected++
                    editor.putString(CNPJ, cnpj_filter)
                }
                val phones_filter = if (binding.viewFilters.phonesFilterTextView.text.equals(InvalidData.UNINITIALIZED.getString())) null else binding.viewFilters.phonesFilterTextView.text.toString()
                if (phones_filter != "") {
                    numOptionsSelected++
                    editor.putString(PHONES, phones_filter)
                }

                var qualificationsSearch = ""
                if (binding.viewFilters.cbA.isChecked) {
                    qualificationsSearch = "A"
                    editor.putInt(CBA, 1)
                } else editor.putInt(CBA, 0)
                if (binding.viewFilters.cbN.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "N" else "$qualificationsSearch;N"
                    editor.putInt(CBN, 1)
                } else editor.putInt(CBN, 0)
                if (binding.viewFilters.cbP.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "P" else "$qualificationsSearch;P"
                    editor.putInt(CBP, 1)
                } else editor.putInt(CBP, 0)
                if (binding.viewFilters.cbR.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "R" else "$qualificationsSearch;R"
                    editor.putInt(CBR, 1)
                } else editor.putInt(CBR, 0)
                if (binding.viewFilters.cbE.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "E" else "$qualificationsSearch;E"
                    editor.putInt(CBE, 1)
                } else editor.putInt(CBE, 0)
                if (binding.viewFilters.cbQ.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "Q" else "$qualificationsSearch;Q"
                    editor.putInt(CBQ, 1)
                } else editor.putInt(CBQ, 0)
                if (binding.viewFilters.cbG.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "G" else "$qualificationsSearch;G"
                    editor.putInt(CBG, 1)
                } else editor.putInt(CBG, 0)
                if (binding.viewFilters.cbI.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "I" else "$qualificationsSearch;I"
                    editor.putInt(CBI, 1)
                } else editor.putInt(CBI, 0)
                if (binding.viewFilters.cbD.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "D" else "$qualificationsSearch;D"
                    editor.putInt(CBD, 1)
                } else editor.putInt(CBD, 0)
                if (binding.viewFilters.cbM.isChecked) {
                    qualificationsSearch = if (qualificationsSearch.isEmpty()) "M" else "$qualificationsSearch;M"
                    editor.putInt(CBM, 1)
                } else editor.putInt(CBN, 0)

                editor.apply()
                if (numOptionsSelected < 3) {
                    showDialogFewOptions(plan, city, speciality, isLocationActive, professionalClass, serviceType, establishmentType,
                        address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter, prof_fantasy_fliter, cnpj_filter, phones_filter, qualificationsSearch)
                } else if (maskValidation) {
                    presenter.clickedButtonSearch(plan, city, speciality, isLocationActive, professionalClass, serviceType, establishmentType,
                        address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter, prof_fantasy_fliter, cnpj_filter, phones_filter, qualificationsSearch)
                }
            }
        }

        binding.buttonCleanFields.setOnClickListener {
            presenter.clickedButtonCleanFields()
        }

        binding.viewFilters.filtersTextView.setOnClickListener {
            pressed = !pressed
            presenter.onAdvancedFilterClicked(pressed)
        }

        binding.orderByDistanceCheckBox.setOnCheckedChangeListener { _, checked ->
            presenter.onOrderByDistanceChanged(checked)
            if (checked) {
                askForPermissions()
            }
        }
    }

    override fun showDialogError(it: Throwable) {
        val listener = {
            presenter.getMedicalGuideOptions()
        }
        showDialogTryAgain(listenerPositiveButton = listener,
            message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showLoading() {
        binding.loginProgressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loginProgressbar.visibility = View.GONE
    }

    override fun selectAllSpinnerToDefault() {
        binding.spinnerSpeciality.selectedIndex = 0
        binding.spinnerCity.selectedIndex = 0
        binding.spinnerPlan.selectedIndex = 0
        binding.viewFilters.spinnerEstablishmentType.selectedIndex = 0
        binding.viewFilters.spinnerServiceType.selectedIndex = 0
        binding.viewFilters.spinnerProfessionalClass.selectedIndex = 0
        binding.viewFilters.cbA.isChecked = false
        binding.viewFilters.cbN.isChecked = false
        binding.viewFilters.cbP.isChecked = false
        binding.viewFilters.cbR.isChecked = false
        binding.viewFilters.cbE.isChecked = false
        binding.viewFilters.cbQ.isChecked = false
        binding.viewFilters.cbG.isChecked = false
        binding.viewFilters.cbI.isChecked = false
        binding.viewFilters.cbD.isChecked = false
        binding.viewFilters.cbM.isChecked = false
        binding.viewFilters.zipcodeFilterTextView.text = null
        binding.viewFilters.addressplanFilterTextView.text = null
        binding.viewFilters.neighborhoodFilterTextView.text = null
        binding.viewFilters.numberOnTheBoardFilterTextView.text = null
        binding.viewFilters.profFantasyCompanyFilterTextView.text = null
        binding.viewFilters.cnpjFilterTextView.text = null
        binding.viewFilters.phonesFilterTextView.text = null
    }

    override fun showAdvancedFilter(pressed: Boolean) {
        if (pressed)
            binding.viewFilters.filtersDetailsContainer.visibility = View.VISIBLE
        else
            binding.viewFilters.filtersDetailsContainer.visibility = View.GONE
    }

    override fun showMedicalGuideOptions(
        presentationMedicalGuideOptions: PresentationMedicalGuideOptions,
        userCodePlan: Int?
    ) {
        plans = mutableListOf(PresentationPlanOptions(description = getString(R.string.text_select)))
        plans.addAll(presentationMedicalGuideOptions.planOptions)
        binding.spinnerPlan.attachDataSource(plans)

        val retPlan = sharedPreferences.getInt(PLAN, 0)
        if (retPlan > 0) binding.spinnerPlan.selectedIndex = retPlan

        userCodePlan?.let {
            plans.forEachIndexed { index, plan ->
                if ((plan.codePlan == userCodePlan) && (retPlan == 0)) {
                    binding.spinnerPlan.selectedIndex = index
                }
            }
        }

        cities = mutableListOf(PresentationCityOptions(description = getString(R.string.text_select)))
        cities.addAll(presentationMedicalGuideOptions.cityOptions)
        binding.spinnerCity.attachDataSource(cities)

        val retCities = sharedPreferences.getInt(CITIES, 0)
        if (retCities > 0) binding.spinnerCity.selectedIndex = retCities

        specialities = mutableListOf(PresentationSpecialityServiceOptions(description = getString(R.string.text_select)))
        specialities.addAll(presentationMedicalGuideOptions.specialityServiceOptions)
        binding.spinnerSpeciality.attachDataSource(specialities)

        val retSpecialities = sharedPreferences.getInt(SPECIALITY, 0)
        if (retSpecialities > 0) binding.spinnerSpeciality.selectedIndex = retSpecialities

        professionalClass = mutableListOf(PresentationProfessionalClass(descriptionProfessionalClass = getString(R.string.text_select)))
        professionalClass.addAll(presentationMedicalGuideOptions.professionalClassOptions)
        binding.viewFilters.spinnerProfessionalClass.attachDataSource(professionalClass)
        val retProfessionalClass = sharedPreferences.getInt(PROFESSIONAL_CLASS, 0)
        if (retProfessionalClass > 0) binding.viewFilters.spinnerProfessionalClass.selectedIndex = retProfessionalClass

        serviceType = mutableListOf(PresentationServiceType(descriptionServiceType = getString(R.string.text_select)))
        serviceType.addAll(presentationMedicalGuideOptions.serviceTypeOptions)
        binding.viewFilters.spinnerServiceType.attachDataSource(serviceType)
        val retServiceType = sharedPreferences.getInt(SERVICE_TYPE, 0)
        if (retServiceType > 0) binding.viewFilters.spinnerServiceType.selectedIndex = retServiceType

        establishmentType = mutableListOf(PresentationEstablishmentType(descriptionEstablishmentType = getString(R.string.text_select)))
        establishmentType.addAll(presentationMedicalGuideOptions.establishmentTypeOptions)
        binding.viewFilters.spinnerEstablishmentType.attachDataSource(establishmentType)
        val retEstablishmentType = sharedPreferences.getInt(ESTABLISHMENT_TYPE, 0)
        if (retEstablishmentType > 0) binding.viewFilters.spinnerEstablishmentType.selectedIndex = retEstablishmentType

        binding.viewFilters.addressplanFilterTextView.setText(sharedPreferences.getString(ADDRESS, EMPTY))
        binding.viewFilters.neighborhoodFilterTextView.setText(sharedPreferences.getString(NEIGHBORHOOD, EMPTY))
        binding.viewFilters.numberOnTheBoardFilterTextView.setText(sharedPreferences.getString(NUMBER_ON_THE_BOARD, EMPTY))
        binding.viewFilters.profFantasyCompanyFilterTextView.setText(sharedPreferences.getString(PROF_FANTASY, EMPTY))

        qualificationsForFilter = mutableListOf(PresentationQualificationForFilter())
        qualificationsForFilter.addAll(presentationMedicalGuideOptions.qualificationOptions)

        adapter = QualificationFilterAdapter(qualificationsForFilter)
        binding.viewFilters.qualificationsFilterRecyclerView.adapter = adapter
        binding.viewFilters.qualificationsFilterRecyclerView.layoutManager = LinearLayoutManager(this)

        for (qualifier in qualificationsForFilter) {
            when (qualifier.cod) {
                "A" -> {
                    binding.viewFilters.imgA.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBA, 0) > 0) binding.viewFilters.cbA.isChecked = true
                }
                "N" -> {
                    binding.viewFilters.imgN.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBN, 0) > 0) binding.viewFilters.cbN.isChecked = true
                }
                "P" -> {
                    binding.viewFilters.imgP.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBP, 0) > 0) binding.viewFilters.cbP.isChecked = true
                }
                "R" -> {
                    binding.viewFilters.imgR.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBR, 0) > 0) binding.viewFilters.cbR.isChecked = true
                }
                "E" -> {
                    binding.viewFilters.imgE.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBE, 0) > 0) binding.viewFilters.cbE.isChecked = true
                }
                "Q" -> {
                    binding.viewFilters.imgQ.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBQ, 0) > 0) binding.viewFilters.cbQ.isChecked = true
                }
                "G" -> {
                    binding.viewFilters.imgG.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBG, 0) > 0) binding.viewFilters.cbG.isChecked = true
                }
                "I" -> {
                    binding.viewFilters.imgI.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBI, 0) > 0) binding.viewFilters.cbI.isChecked = true
                }
                "D" -> {
                    binding.viewFilters.imgD.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBD, 0) > 0) binding.viewFilters.cbD.isChecked = true
                }
                "M" -> {
                    binding.viewFilters.imgM.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                    if (sharedPreferences.getInt(CBM, 0) > 0) binding.viewFilters.cbM.isChecked = true
                }
            }
        }
    }

    override fun getCurrentLocation() {
        LocationHelper.getCurrentLocation(this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    presenter.onLocationFetched(it)
                },
                onError = {
                    it.printStackTrace()
                    if (it is LocationHelper.LocationNotEnabledException) {
                        presenter.onLocationNotEnabled()
                    }
                }
            )
    }

    override fun showNeedPermissionsDialog(anyPermissionPermanentlyDenied: Boolean) {
        DialogHelper.showDialog(
            this,
            R.string.title_permissions_needed,
            R.string.text_permissions_needed,
            R.string.text_ok,
            null
        )
    }

    override fun showLocationMissingError() {
        DialogHelper.showDialog(
            this,
            R.string.title_permissions_needed,
            R.string.text_location_missing_gps_off,
            R.string.text_ok,
            null
        )
    }

    override fun showLocationFetchErrorDialog() {
        DialogHelper.showDialogTryAgain(
            this,
            { askForPermissions() },
            getString(R.string.text_location_missing_error)
        )
    }

    override fun showLocationNotEnabledDialog() {
        DialogHelper.showDialog(
            this,
            R.string.title_error_oops,
            R.string.text_location_not_enabled,
            R.string.text_ok,
            R.string.action_cancel,
            { presenter.onLocationNotEnabledDialogOkClicked() })
    }

    override fun setLocationCheckbox(checked: Boolean) {
        binding.orderByDistanceCheckBox.isChecked = checked
    }

    override fun showDialogFewOptions(
        plan: PresentationPlanOptions,
        city: PresentationCityOptions,
        speciality: PresentationSpecialityServiceOptions,
        isLocationActive: Boolean,
        professionalClass: PresentationProfessionalClass,
        serviceType: PresentationServiceType,
        establishmentType: PresentationEstablishmentType,
        address_filter: String?,
        neighborhood_filter: String?,
        zipcode_filter: String?,
        number_on_the_board_filter: String?,
        prof_fantasy_fliter: String?,
        cnpj_filter: String?,
        phones_filter: String?,
        qualificationsSearch: String?
    ) {
        DialogHelper.showDialog(
            this,
            R.string.title_few_options,
            R.string.text_few_options,
            R.string.global_yes,
            R.string.action_cancel,
            {
                presenter.clickedButtonSearch(
                    plan, city, speciality, isLocationActive, professionalClass, serviceType,
                    establishmentType, address_filter, neighborhood_filter, zipcode_filter,
                    number_on_the_board_filter, prof_fantasy_fliter, cnpj_filter, phones_filter, qualificationsSearch
                )
            })
    }

    override fun clickedLink() {
        IntentHelper.openUrlInBrowser(this, getString(R.string.url_custom_infos))
    }

    override fun setLocationActive(locationActive: Boolean) {
        isLocationActive = locationActive
        if (isLocationActive) {
            askForPermissions()
            if (!isGPSEnable())
                showLocationMissingError()
        }
    }

    override fun isGPSEnable(): Boolean {
        return (this.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onResume() {
        super.onResume()
        binding.viewFilters.zipcodeFilterTextView.addTextChangedListener(zipcodeMask)
        binding.viewFilters.cnpjFilterTextView.addTextChangedListener(cnpjMask)
        binding.viewFilters.phonesFilterTextView.addTextChangedListener(phoneMask)
    }
}
