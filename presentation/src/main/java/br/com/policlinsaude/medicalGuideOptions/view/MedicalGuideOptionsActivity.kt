package br.com.policlinsaude.medicalGuideOptions.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.data.datasource.networking.rest.model.JsonQualificationsForFilter
import br.com.domain.exception.MessageErrorException
import br.com.domain.helper.InvalidData
import br.com.domain.model.ServiceTypeOptions
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.LocationHelper
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.medicalGuideOptions.presenter.MedicalGuideOptionsPresenter
import br.com.policlinsaude.model.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_medical_guide_options.*
import kotlinx.android.synthetic.main.view_filters.*
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

    //Andre Inicio
    private  var pressed: Boolean = false // controle exibicao Filtro Avançado
    private  var isLocationActive: Boolean = false // controle exibicao Filtro Avançado

    private lateinit var professionalClass:  MutableList<PresentationProfessionalClass>
    private lateinit var serviceType:  MutableList<PresentationServiceType>
    private lateinit var establishmentType: MutableList<PresentationEstablishmentType>
    private lateinit var qualificationsForFilter: MutableList<PresentationQualificationForFilter>

    //Armazenar filtro antes de ir para o Guia
    private var EMPTY = ""
    private var myPreferences = "myPrefs"
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

    private lateinit var sharedPreferences: SharedPreferences

    //Andre
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FILTRO", "no ONCREATE")

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(CITIES,0)
        editor.putInt(SPECIALITY,0)
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

        setContentView(R.layout.activity_medical_guide_options)
        AndroidInjection.inject(this)
        showMedicalGuideOptions(PresentationMedicalGuideOptions(), null)

        presenter.getLocationPreference()



        setupToolbar()

        setOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        Log.d("FILTRO", "no ONSTART")


        presenter.getMedicalGuideOptions()
    }

    override fun askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        }
    }

    private fun setOnClickListeners() {
        buttonSearch.setOnClickListener {



            if (awesomeValidation.validate()) {

                var numOptionsSelected: Int = 0

                val editor = sharedPreferences.edit()

                //Andre

                val plan = plans[spinnerPlan.selectedIndex]
                if (!(spinnerPlan.selectedIndex == 0) ){
                    numOptionsSelected++

                }
                Log.d("MEDICALGUIDELISTOPTIONS","Plano:--" + plan+"--")


                val city = cities[spinnerCity.selectedIndex]
                if (!(spinnerCity.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(CITIES, spinnerCity.selectedIndex)

                }
                Log.d("MEDICALGUIDELISTOPTIONS","Cidade: " + city + " indice da combo: " + spinnerCity.selectedIndex )

                val speciality = specialities[spinnerSpeciality.selectedIndex]
                if (!(spinnerSpeciality.selectedIndex == 0)){
                    numOptionsSelected++
                    editor.putInt(SPECIALITY, spinnerSpeciality.selectedIndex)


                }

                Log.d("MEDICALGUIDELISTOPTIONS","Especialidade: " + speciality +  " indice da combo: " + spinnerSpeciality.selectedIndex)

                val orderByDistance = order_by_distance_check_box.isChecked
            /*    if (order_by_distance_check_box.isChecked)  numOptionsSelected++
                */

                val professionalClass  = professionalClass[spinnerProfessionalClass.selectedIndex]
                if (!(spinnerProfessionalClass.selectedIndex == 0)) {
                    numOptionsSelected++
                    editor.putInt(PROFESSIONAL_CLASS, spinnerProfessionalClass.selectedIndex)
                 }


                val serviceType = serviceType[spinnerServiceType.selectedIndex]
                if (!(spinnerServiceType.selectedIndex == 0))  {
                    numOptionsSelected++
                    editor.putInt(SERVICE_TYPE, spinnerServiceType.selectedIndex)
                }

                val establishmentType = establishmentType[spinnerEstablishmentType.selectedIndex]
                if (!(spinnerEstablishmentType.selectedIndex == 0))  {
                    numOptionsSelected++
                    editor.putInt(ESTABLISHMENT_TYPE, spinnerEstablishmentType.selectedIndex)
                }
               // Log.d("MEDICALGUIDELISTOPTIONS","Total ate especialiade: " + numOptionsSelected)

                //demais campos do filtro

                //endereço
                val address_filter      =  if (addressplan_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  addressplan_filter_text_view.text.toString()  //Andre
                if (address_filter != "") {
                    numOptionsSelected++
                    editor.putString(ADDRESS, address_filter)
                }
               // Log.d("MEDICALGUIDELISTOPTIONS","Valor de address_filter: " + address_filter)
               // Log.d("MEDICALGUIDELISTOPTIONS","Total ate especialiade: " + numOptionsSelected)
                val neighborhood_filter =  if (neighborhood_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  neighborhood_filter_text_view.text.toString()  //Andre
                if (neighborhood_filter != "") {
                    numOptionsSelected++
                    editor.putString(NEIGHBORHOOD, neighborhood_filter)
                }

                val zipcode_filter      =  if (zipcode_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  zipcode_filter_text_view.text.toString()  //Andre
                if (zipcode_filter != "") {
                    numOptionsSelected++
                    editor.putString(ZIPCODE, zipcode_filter)
                }

                val number_on_the_board_filter =  if (number_on_the_board_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  number_on_the_board_filter_text_view.text.toString()  //Andre
                if (number_on_the_board_filter != "") {
                    numOptionsSelected++
                    editor.putString(NUMBER_ON_THE_BOARD, number_on_the_board_filter)
                }

                val prof_fantasy_fliter =  if (prof_fantasy_company_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  prof_fantasy_company_filter_text_view.text.toString()  //Andre
                if (prof_fantasy_fliter != "") {
                    numOptionsSelected++
                    editor.putString(PROF_FANTASY, prof_fantasy_fliter)
                }

                val cnpj_filter =  if (cnpj_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  cnpj_filter_text_view.text.toString()  //Andre
                if (cnpj_filter != "") {
                    numOptionsSelected++
                    editor.putString(CNPJ, cnpj_filter)
                }

                val phones_filter =  if (phones_filter_text_view.text.equals(InvalidData.UNINITIALIZED.getString())) null else  phones_filter_text_view.text.toString()  //Andre
                if (phones_filter != "") {
                    numOptionsSelected++
                    editor.putString(PHONES, phones_filter)
                }
                var qualificationsSearch = ""

                if (cbA.isChecked) {
                    qualificationsSearch = "A"
                    editor.putInt(CBA, 1)
                }else editor.putInt(CBA, 0)

                if ((cbN.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";N"
                }
                if (cbN.isChecked) {
                    qualificationsSearch = "N"
                    editor.putInt(CBN, 1)
                } else editor.putInt(CBN, 0)

                if ((cbP.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";P"
                }
                if (cbP.isChecked){
                    qualificationsSearch = "P"
                    editor.putInt(CBP, 1)
                } else editor.putInt(CBP, 0)

                if ((cbR.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";R"
                 }
                if (cbR.isChecked){
                    qualificationsSearch = "R"
                    editor.putInt(CBR, 1)
                } else editor.putInt(CBR, 0)

                if ((cbE.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";E"
                }
                if (cbE.isChecked){
                    qualificationsSearch = "E"
                    editor.putInt(CBE, 1)
                } else editor.putInt(CBE, 0)

                if ((cbQ.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";Q"
                }
                if (cbQ.isChecked){
                    qualificationsSearch = "Q"
                    editor.putInt(CBQ, 1)
                } else editor.putInt(CBQ, 0)

                if ((cbG.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";G"
                }
                if (cbG.isChecked){
                    qualificationsSearch = "G"
                    editor.putInt(CBG, 1)
                } else editor.putInt(CBG, 0)

                if ((cbI.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";I"
                }
                if (cbI.isChecked){
                    qualificationsSearch = "I"
                    editor.putInt(CBI, 1)
                 } else editor.putInt(CBI, 0)

                if ((cbD.isChecked) && (qualificationsSearch.isNotEmpty())){
                }
                if (cbD.isChecked){
                    qualificationsSearch = "D"
                    editor.putInt(CBD, 1)
                } else editor.putInt(CBD, 0)

                if ((cbM.isChecked) && (qualificationsSearch.isNotEmpty())){
                    qualificationsSearch = qualificationsSearch +";M"
                 }
                if (cbM.isChecked) {
                    qualificationsSearch = "M"
                    editor.putInt(CBM, 1)
                } else editor.putInt(CBN, 0)

                Log.d("MEDICALGUIDELISTOPTIONS","QUALIFICADORES: " + qualificationsSearch)

                //Andre ----- fim
                Log.d("MEDICALGUIDELISTOPTIONS","Opções selecionadas: " + numOptionsSelected)
                Log.d("MEDICALGUIDELISTOPTIONS","Phones: " + phones_filter)
                Log.d("MEDICALGUIDELISTOPTIONS","Nome em fantasy: " + prof_fantasy_fliter)


                editor.apply()

                if (numOptionsSelected < 3){

                    showDialogFewOptions(plan, city, speciality, isLocationActive, professionalClass, serviceType, establishmentType,
                            address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter,prof_fantasy_fliter, cnpj_filter, phones_filter, qualificationsSearch)
                }
                else{
                    //presenter.clickedButtonSearch(plan, city, speciality, orderByDistance, professionalClass, serviceType, establishmentType,
                    presenter.clickedButtonSearch(plan, city, speciality, isLocationActive, professionalClass, serviceType, establishmentType,
                            address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter,prof_fantasy_fliter, cnpj_filter, phones_filter, qualificationsSearch)
                }
            }
        }

        buttonCleanFields.setOnClickListener {
            presenter.clickedButtonCleanFields()
        }

        filters_text_view.setOnClickListener{
            pressed = !pressed
            presenter.onAdvancedFilterClicked(pressed)

        }

        order_by_distance_check_box.setOnCheckedChangeListener { _, checked ->
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
        login_progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        login_progressbar.visibility = View.GONE
    }

    override fun selectAllSpinnerToDefault() {
        spinnerSpeciality.selectedIndex = 0
        spinnerCity.selectedIndex = 0
        spinnerPlan.selectedIndex = 0
        spinnerEstablishmentType.selectedIndex = 0
        spinnerServiceType.selectedIndex = 0
        spinnerProfessionalClass.selectedIndex = 0
        cbA.isChecked = false
        cbN.isChecked = false
        cbP.isChecked = false
        cbR.isChecked = false
        cbE.isChecked = false
        cbQ.isChecked = false
        cbG.isChecked = false
        cbI.isChecked = false
        cbD.isChecked = false
        cbM.isChecked = false
        zipcode_filter_text_view.text = null
        addressplan_filter_text_view.text = null
        neighborhood_filter_text_view.text = null
        number_on_the_board_filter_text_view.text = null
        prof_fantasy_company_filter_text_view.text = null
        cnpj_filter_text_view.text = null
        phones_filter_text_view.text = null
    }

    override fun showAdvancedFilter(pressed: Boolean){
        if (pressed)
            filters_details_container.visibility = View.VISIBLE
        else
            filters_details_container.visibility = View.GONE
    }

    override fun showMedicalGuideOptions(presentationMedicalGuideOptions:
                                         PresentationMedicalGuideOptions, userCodePlan: Int?) {

        Log.d("FILTRO", "no showMedicalGuideOptions")

        plans = mutableListOf(PresentationPlanOptions(description = getString(R.string.text_select)))
        plans.addAll(presentationMedicalGuideOptions.planOptions)
        spinnerPlan.attachDataSource(plans)

        userCodePlan?.let {
            plans.forEachIndexed({ index, plan ->
                if (plan.codePlan == userCodePlan) {
                    spinnerPlan.selectedIndex = index
                }
            })
        }

        cities = mutableListOf(PresentationCityOptions(description = getString(R.string.text_select)))
        cities.addAll(presentationMedicalGuideOptions.cityOptions)
        spinnerCity.attachDataSource(cities)

        var retCities: Int = sharedPreferences.getInt(CITIES, 0)

        Log.d("MEDICALGUIDELISTOPTIONS", "VALOR DE retCITIES: " + retCities)

        if (retCities > 0) spinnerCity.selectedIndex = retCities



        specialities = mutableListOf(PresentationSpecialityServiceOptions(description = getString(R.string.text_select)))
        specialities.addAll(presentationMedicalGuideOptions.specialityServiceOptions)
        spinnerSpeciality.attachDataSource(specialities)

        var retSpecialities: Int = sharedPreferences.getInt(SPECIALITY, 0)

        if (retSpecialities > 0) spinnerSpeciality.selectedIndex = retSpecialities


        //Andre ------- inicio
        professionalClass = mutableListOf(PresentationProfessionalClass(descriptionProfessionalClass = getString(R.string.text_select)))
        professionalClass.addAll(presentationMedicalGuideOptions.professionalClassOptions)
        spinnerProfessionalClass.attachDataSource(professionalClass)
        var retProfessionalClass: Int = sharedPreferences.getInt(PROFESSIONAL_CLASS, 0)

        if (retProfessionalClass > 0) spinnerProfessionalClass.selectedIndex = retProfessionalClass

        serviceType = mutableListOf(PresentationServiceType(descriptionServiceType = getString(R.string.text_select)))
        serviceType.addAll(presentationMedicalGuideOptions.serviceTypeOptions)
        spinnerServiceType.attachDataSource(serviceType)

        var retServiceType: Int = sharedPreferences.getInt(SERVICE_TYPE, 0)
        if (retServiceType > 0) spinnerServiceType.selectedIndex = retServiceType



        establishmentType = mutableListOf(PresentationEstablishmentType(descriptionEstablishmentType = getString(R.string.text_select)))
        establishmentType.addAll(presentationMedicalGuideOptions.establishmentTypeOptions)
        spinnerEstablishmentType.attachDataSource(establishmentType)

        var retEstablishmentType: Int = sharedPreferences.getInt(ESTABLISHMENT_TYPE, 0)
        if (retEstablishmentType > 0) spinnerEstablishmentType.selectedIndex = retEstablishmentType

        addressplan_filter_text_view.setText(sharedPreferences.getString(ADDRESS, EMPTY))
        neighborhood_filter_text_view.setText(sharedPreferences.getString(NEIGHBORHOOD, EMPTY))
        zipcode_filter_text_view.setText(sharedPreferences.getString(ZIPCODE, EMPTY))
        number_on_the_board_filter_text_view.setText(sharedPreferences.getString(ZIPCODE, EMPTY))
        prof_fantasy_company_filter_text_view.setText(sharedPreferences.getString(PROF_FANTASY, EMPTY))
        cnpj_filter_text_view.setText(sharedPreferences.getString(CNPJ, EMPTY))
        phones_filter_text_view.setText(sharedPreferences.getString(PHONES, EMPTY))

        qualificationsForFilter = mutableListOf(PresentationQualificationForFilter())
        qualificationsForFilter.addAll(presentationMedicalGuideOptions.qualificationOptions)

        for (qualifier in qualificationsForFilter){

            if (qualifier.cod == "A"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO A: " + qualifier.imgQualificacao)
               imgA.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
               var retA = sharedPreferences.getInt(CBA, 0)
                if (retA > 0 ) cbA.isChecked =  true

            }
            if (qualifier.cod == "N"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO N: " + qualifier.imgQualificacao)
                imgN.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retN = sharedPreferences.getInt(CBN, 0)
                if (retN > 0 ) cbN.isChecked =  true
            }

            if (qualifier.cod == "P"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO P: " + qualifier.imgQualificacao)
                imgP.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retP = sharedPreferences.getInt(CBP, 0)
                if (retP > 0 ) cbP.isChecked =  true

            }
            if (qualifier.cod == "R"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO R: " + qualifier.imgQualificacao)
                imgR.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retR = sharedPreferences.getInt(CBR, 0)
                if (retR > 0 ) cbR.isChecked =  true

            }
            if (qualifier.cod == "E"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO E: " + qualifier.imgQualificacao)
                imgE.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retE = sharedPreferences.getInt(CBE, 0)
                if (retE > 0 ) cbE.isChecked =  true

            }
            if (qualifier.cod == "Q"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO Q: " + qualifier.imgQualificacao)
                imgQ.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retQ = sharedPreferences.getInt(CBQ, 0)
                if (retQ > 0 ) cbQ.isChecked =  true

            }

            if (qualifier.cod == "G"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO G: " + qualifier.imgQualificacao)
                imgG.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retG = sharedPreferences.getInt(CBG, 0)
                if (retG > 0 ) cbG.isChecked =  true

            }
            if (qualifier.cod == "I"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO I: " + qualifier.imgQualificacao)
                imgI.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retI = sharedPreferences.getInt(CBI, 0)
                if (retI > 0 ) cbI.isChecked =  true

            }
            if (qualifier.cod == "D"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO D: " + qualifier.imgQualificacao)
                imgD.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retD = sharedPreferences.getInt(CBD, 0)
                if (retD > 0 ) cbD.isChecked =  true

            }
            if (qualifier.cod == "M"){
                Log.d("Andre", "DENTRO DO CHECKBOX DO M: " + qualifier.imgQualificacao)
                imgM.setImageBitmap(qualifier.imgQualificacao.getBitmapFromImage())
                var retM = sharedPreferences.getInt(CBM, 0)
                if (retM > 0 ) cbM.isChecked =  true

            }

        }

       Log.d("Andre", "Valor de lista de ProfessionalClass: " + presentationMedicalGuideOptions.professionalClassOptions.toString())

        Log.d("Andre", "Valor de lista de Tipo de Serviço: " + presentationMedicalGuideOptions.serviceTypeOptions.toString())
        Log.d("Andre", "Valor de lista de Tipo de Estabelecimento: " + presentationMedicalGuideOptions.establishmentTypeOptions.toString())
        Log.d("Andre", "Valor de lista de QUALIFICAÇÔES " + presentationMedicalGuideOptions.qualificationOptions.toString())
    }

    override fun getCurrentLocation() {
        LocationHelper.getCurrentLocation(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            presenter.onLocationFetched(it)
                            Log.d("Andre", "DENTRO de getCurrentLocation: --> "+ it)
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
        DialogHelper.showDialog(this,
                R.string.title_permissions_needed,
                R.string.text_permissions_needed,
                R.string.text_ok,
                null)
               // listenerPositiveButton = { presenter.onPermissionsNeedDialogOkClicked(anyPermissionPermanentlyDenied) },
               // onDismiss = { presenter.onPermissionsNeedDialogOkClicked(anyPermissionPermanentlyDenied) })
    }

    override fun showLocationMissingError() {
        DialogHelper.showDialog(this,
                R.string.title_permissions_needed,
                R.string.text_location_missing_gps_off,
                R.string.text_ok,
                null)//,
               // listenerPositiveButton = {finish() })

     /*   DialogHelper.showDialogTryAgain(this,
                { askForPermissions() },
               // getString(R.string.text_location_missing_error))
                getString(R.string.text_location_missing_gps_off))*/

    }

    override fun showLocationFetchErrorDialog() {
        DialogHelper.showDialogTryAgain(this,
                { askForPermissions() },
                getString(R.string.text_location_missing_error))
    }

    override fun showLocationNotEnabledDialog() {
        DialogHelper.showDialog(this,
                R.string.title_error_oops,
                R.string.text_location_not_enabled,
                R.string.text_ok,
                R.string.action_cancel,
                { presenter.onLocationNotEnabledDialogOkClicked() })

    }

    override fun setLocationCheckbox(checked: Boolean) {
        order_by_distance_check_box.isChecked = checked
    }


    override fun showDialogFewOptions(plan: PresentationPlanOptions,
                                      city: PresentationCityOptions,
                                      speciality: PresentationSpecialityServiceOptions,
                                      //orderByDistance: Boolean,
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
                                      phones_filter: String?, qualificationsSearch: String?){
       DialogHelper.showDialog(this,
                R.string.title_few_options,
                R.string.text_few_options,
                R.string.global_yes,
                R.string.action_cancel,
               { presenter.clickedButtonSearch(plan, city, speciality, isLocationActive, professionalClass, serviceType,
                       establishmentType, address_filter, neighborhood_filter, zipcode_filter,
                       number_on_the_board_filter,prof_fantasy_fliter, cnpj_filter, phones_filter, qualificationsSearch) }  )


    }

    override fun setLocationActive(locationActive: Boolean) {
        Log.d("Andre", "DENTRO do setLocationActive: locationActive --> " + locationActive)

       isLocationActive = locationActive
        Log.d("Andre", "DENTRO do setLocationActive: isLocationActive--> " + isLocationActive)

        if (isLocationActive) {

            askForPermissions()

            Log.d("Andre", "isLocationActive esta ativo")
            if (!isGPSEnable())
                showLocationMissingError()


        } else  Log.d("Andre", "isLocationActive esta desativado")
    }

    override fun isGPSEnable(): Boolean{

       val  isOn:Boolean = (this.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

       if (isOn)  Log.d("Andre", "GPS LIGADO ")
        else if (isOn)  Log.d("Andre", "GPS DESLIGADO ")

        return (isOn)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        // do nothing
        // this is because the amount of data in the pagers
        Log.d("FILTRO", "DENTRO do onSaveInstanceState de MedicalguideOptionsActivity")

        if (outState != null) {
        //    outState.putString("MyString", "Welcome back to Android")
        //    Log.d("FILTRO", "SALVANDO MyString no onSaveInstanceState de MedicalguideOptionsActivity")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("FILTRO", "DENTRO do onRestoreInstanceState de MedicalguideOptionsActivity")

      //  val myString:String = savedInstanceState!!.getString("MyString")
      //  Log.d("FILTRO", "DENTRO do onRestoreInstanceState de MedicalguideOptionsActivity --> Valor de MyString: " + myString)
    }


    override fun onPause() {
        super.onPause()
        Log.d("FILTRO", "DENTRO do onPause de MedicalguideOptionsActivity --> ")

    }

    override fun onResume() {
        super.onResume()
        Log.d("FILTRO", "DENTRO do onResume de MedicalguideOptionsActivity --> ")

    }
}