package br.com.policlinsaude.medicalGuideList.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.medicalGuideList.presenter.MedicalGuideListPresenter
import br.com.policlinsaude.medicalGuideList.view.adapter.CityLevelAdapter
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideListAdapter
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.*
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_medical_guide_list.*
import kotlinx.android.synthetic.main.view_filters.*
import kotlinx.android.synthetic.main.view_filters.view.*
import javax.inject.Inject

class MedicalGuideListActivity : BaseActivity(), MedicalGuideListView, MedicalGuideListAdapter.OnItemClickListener {

    companion object {

        private const val BUNDLE_EXTRA_PLAN_OPTION: String = "plan_option"
        private const val BUNDLE_EXTRA_CITY_OPTION: String = "city_option"
        private const val BUNDLE_EXTRA_SPECIALITY_OPTION: String = "speciality_option"
        private const val BUNDLE_EXTRA_LOCATION: String = "location"
        //Andre
        private const val BUNDLE_EXTRA_PROFESSIONAL_CLASS: String = "professional_class"
        private const val BUNDLE_EXTRA_SERVICE_TYPE: String = "service_type"
        private const val BUNDLE_EXTRA_ESTABLISHMENT_TYPE: String = "establishment_type"

        //Andre
        private const val ADDRESS_FILTER = "address_filter"
        private const val NEIGHBORHOOD_FILTER = "neighborhood_filter"
        private const val ZIPCODE_FILTER = "zipcode_filter"
        private const val NUMBER_ON_THE_BOARD_FILTER = "number_on_the_board_filter"
        private const val PROF_FANTASY_FILTER = "prof_fantasy_filter"
        private const val CNPJ_FILTER = "cnpj_filter"
        private const val PHONES_FILTER = "phones_filter"
        private const val QUALIFIERS = "FilAvanc_Qualificacoes"
        //Andre --fim
        fun start(activity: Activity,
                  planOptions: PresentationPlanOptions,
                  cityOptions: PresentationCityOptions,
                  specialityOptions: PresentationSpecialityServiceOptions,
                  location: PresentationLocation?,
                  professionalClass: PresentationProfessionalClass,
                  service_type : PresentationServiceType,
                  establishmentType: PresentationEstablishmentType,
                  address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                  prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?)//Andre
         {
            val intent = Intent(activity, MedicalGuideListActivity::class.java)
            val bundle = Bundle()
            bundle.apply {
                putSerializable(BUNDLE_EXTRA_PLAN_OPTION, planOptions)
                putSerializable(BUNDLE_EXTRA_CITY_OPTION, cityOptions)
                putSerializable(BUNDLE_EXTRA_SPECIALITY_OPTION, specialityOptions)
                putSerializable (BUNDLE_EXTRA_PROFESSIONAL_CLASS, professionalClass)
                putSerializable (BUNDLE_EXTRA_SERVICE_TYPE, service_type)
                putSerializable (BUNDLE_EXTRA_ESTABLISHMENT_TYPE, establishmentType)
                //Andre
                putSerializable (ADDRESS_FILTER, address_filter)
                putSerializable (NEIGHBORHOOD_FILTER, neighborhood_filter)
                putSerializable (ZIPCODE_FILTER, zipcode_filter)
                putSerializable (NUMBER_ON_THE_BOARD_FILTER, number_on_the_board_filter)
                putSerializable (PROF_FANTASY_FILTER, prof_fantasy_filter)
                putSerializable (CNPJ_FILTER, cnpj_filter)
                putSerializable (PHONES_FILTER, phones_filter)
                putSerializable (QUALIFIERS, qualificationsSearch)

                //Andre -- fim
                location?.let { putParcelable(BUNDLE_EXTRA_LOCATION, it)
                }
            }
             Log.d("Andre", "MedicalGuideListActivity : valor de nome; " + prof_fantasy_filter + "  valor de phone: " + phones_filter )
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: MedicalGuideListPresenter

    lateinit var adapter: MedicalGuideListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_guide_list)
        AndroidInjection.inject(this)

        setupToolbar()
        adapter = MedicalGuideListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(this)
        setupOnClickListener()
        getMedicalGuideOptions()
    }

    private fun setupOnClickListener() {
        view_filters.filters_text_view.setOnClickListener {
            view_filters.filters_details_container.visibility =
                    if (view_filters.filters_details_container.visibility == View.VISIBLE) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
        }
    }

    private fun getMedicalGuideOptions() {
             presenter.getMedicalGuideList(intent.getSerializableExtra(BUNDLE_EXTRA_PLAN_OPTION) as PresentationPlanOptions,
                intent.getSerializableExtra(BUNDLE_EXTRA_CITY_OPTION) as PresentationCityOptions,
                intent.getSerializableExtra(BUNDLE_EXTRA_SPECIALITY_OPTION) as PresentationSpecialityServiceOptions,
                intent.getSerializableExtra(BUNDLE_EXTRA_PROFESSIONAL_CLASS) as PresentationProfessionalClass,//Andre
                intent.getSerializableExtra(BUNDLE_EXTRA_SERVICE_TYPE) as PresentationServiceType,//Andre
                intent.getSerializableExtra(BUNDLE_EXTRA_ESTABLISHMENT_TYPE) as PresentationEstablishmentType,//Andre
                intent.getSerializableExtra(ADDRESS_FILTER) as String,//Andre
                intent.getSerializableExtra(NEIGHBORHOOD_FILTER) as String,//Andre
                intent.getSerializableExtra(ZIPCODE_FILTER) as String,//Andre
                intent.getSerializableExtra(NUMBER_ON_THE_BOARD_FILTER) as String,//Andre
                intent.getSerializableExtra(PROF_FANTASY_FILTER) as String,//Andre
                intent.getSerializableExtra(CNPJ_FILTER) as String,//Andre
                intent.getSerializableExtra(PHONES_FILTER) as String,//Andre //
                intent.getSerializableExtra(QUALIFIERS) as String,
                intent.getParcelableExtra(BUNDLE_EXTRA_LOCATION))
    }

    override fun onResume() {
        super.onResume()
        getMedicalGuideOptions()
    }

    override fun showMedicalGuideList(presentationMedicalGuideList: PresentationMedicalGuideList) {

        var listPlansV4List: List<PresentationMedicalGuideListPlansV4>
        //var establishmentList: List<PresentationEstablishment>? = null

        var establishmentList: MutableList<PresentationEstablishment> = ArrayList()

        var itemEstablishment: PresentationEstablishment
        var citiesList: List <PresentationMedicalGuideListCitiesV4>
        var serviceTypesV4List: List<PresentationMedicalGuideListServiceTypesV4>
        var especialityList: List<PresentationMedicalGuideListSpecialitiesV4>
        var medicalGuideV4: List<PresentationEstablishment>



        listPlansV4List = presentationMedicalGuideList.medicalGuideListPlansV4.toMutableList()

        var flagPlan = false


        for (itPlan in 0..listPlansV4List.size-1){
            //for (item in listPlansV4List){

            if (!flagPlan)
                flagPlan = true


            var flagCity = false

            //  citiesList = item.cities.toMutableList()
            citiesList = listPlansV4List.get(itPlan).cities.toMutableList()

            for (itCity in 0..citiesList.size-1){

                if (!flagCity)
                    flagCity = true


                serviceTypesV4List = citiesList.get(itCity).serviceType.toMutableList()


                var flagServiceType = false


                //for (serviceType in serviceTypesV4List) {
                for (itServiceType in 0..serviceTypesV4List.size-1){
                    //   itemEstablishment?.serviceTypeV4 = serviceType.serviceType

                    if (!flagServiceType)
                        flagServiceType = true

                    var serviceTypeRef: Int = itServiceType
                    //especialityList = serviceType.specialities.toMutableList()
                    especialityList = serviceTypesV4List.get(itServiceType).specialities.toMutableList()


                    var flagEspeciality = false

                    for (itEspeciality in 0..especialityList.size -1){
                        //for (especiality in especialityList) {

                        if (!flagEspeciality)
                            flagEspeciality = true

                        //medicalGuideV4 = especiality.medicalGuide.toMutableList()
                        medicalGuideV4 = especialityList.get(itEspeciality).medicalGuide.toMutableList()

                        var i:Int = 0

                        for (itMedicalGuide in 0..medicalGuideV4.size -1){
                            //for (medicalGuide in medicalGuideV4){


                            itemEstablishment = PresentationEstablishment()



                            if (itMedicalGuide == 0)
                                itemEstablishment.showEspecialityV4 = 1
                            else itemEstablishment.showEspecialityV4 = 0


                            if (flagServiceType){
                                itemEstablishment.showServiceTypesV4 = 1
                                flagServiceType = false
                            }

                           if (flagCity)
                                itemEstablishment.showCityV4 = 1
                                flagCity = false


                            if (flagPlan) {
                                itemEstablishment.showPlansV4 = 1
                                flagPlan = false
                            }


                            itemEstablishment.planNameV4  = listPlansV4List.get(itPlan).planName
                            itemEstablishment.cityV4 = citiesList.get(itCity).cityName
                            itemEstablishment.serviceTypeV4 = serviceTypesV4List.get(itServiceType).serviceType

                            itemEstablishment.especialityNameV4 = especialityList.get(itEspeciality).specialityName

                            itemEstablishment.name = medicalGuideV4.get(itMedicalGuide).name
                            itemEstablishment.socialName = medicalGuideV4.get(itMedicalGuide).socialName
                            itemEstablishment.cnpj = medicalGuideV4.get(itMedicalGuide).cnpj
                            itemEstablishment.qualifications = medicalGuideV4.get(itMedicalGuide).qualifications
                            itemEstablishment.type = medicalGuideV4.get(itMedicalGuide).type

                            itemEstablishment.title = medicalGuideV4.get(itMedicalGuide).title
                            itemEstablishment.subTitle = medicalGuideV4.get(itMedicalGuide).subTitle
                            itemEstablishment.speciality = medicalGuideV4.get(itMedicalGuide).especialityNameV4
                            itemEstablishment.speciality = medicalGuideV4.get(itMedicalGuide).speciality
                            itemEstablishment.publicPlace = medicalGuideV4.get(itMedicalGuide).publicPlace
                            itemEstablishment.number = medicalGuideV4.get(itMedicalGuide).number
                            itemEstablishment.neighborhood = medicalGuideV4.get(itMedicalGuide).neighborhood
                            itemEstablishment.zipCode = medicalGuideV4.get(itMedicalGuide).zipCode
                            itemEstablishment.complement = medicalGuideV4.get(itMedicalGuide).complement
                            itemEstablishment.city = medicalGuideV4.get(itMedicalGuide).city
                            itemEstablishment.state = medicalGuideV4.get(itMedicalGuide).state
                            itemEstablishment.phoneOne = medicalGuideV4.get(itMedicalGuide).phoneOne
                            itemEstablishment.phoneTwo = medicalGuideV4.get(itMedicalGuide).phoneTwo
                            itemEstablishment.typePhoneOne = medicalGuideV4.get(itMedicalGuide).typePhoneOne
                            itemEstablishment.typePhoneTwo = medicalGuideV4.get(itMedicalGuide).typePhoneTwo
                            itemEstablishment.latitude = medicalGuideV4.get(itMedicalGuide).latitude
                            itemEstablishment.longitude = medicalGuideV4.get(itMedicalGuide).longitude
                            itemEstablishment.distance = medicalGuideV4.get(itMedicalGuide).distance
                            itemEstablishment.photoFront = medicalGuideV4.get(itMedicalGuide).photoFront
                            itemEstablishment.cidCod = medicalGuideV4.get(itMedicalGuide).cidCod
                            itemEstablishment.esCod = medicalGuideV4.get(itMedicalGuide).esCod
                            itemEstablishment.prsSeq = medicalGuideV4.get(itMedicalGuide).prsSeq
                            itemEstablishment.prsCod = medicalGuideV4.get(itMedicalGuide).prsCod
                            itemEstablishment.proUf = medicalGuideV4.get(itMedicalGuide).proUf
                            itemEstablishment.proCls = medicalGuideV4.get(itMedicalGuide).proCls
                            itemEstablishment.proCod = medicalGuideV4.get(itMedicalGuide).proCod
                            itemEstablishment.isOwnNetwork = medicalGuideV4.get(itMedicalGuide).isOwnNetwork
                            itemEstablishment.favorited = medicalGuideV4.get(itMedicalGuide).favorited
                            itemEstablishment.uType = medicalGuideV4.get(itMedicalGuide).uType



                            Log.d("PRESENTATIONACTIVITY","-----------------------------------")
                            Log.d("PRESENTATIONACTIVITY","itMedicalGuide TOSTRING; " + medicalGuideV4.get(itMedicalGuide).qualifications.toString())
                          //  Log.d("PRESENTATIONACTIVITY","DESCRICAO: " + itemEstablishment.qualifications.get(itMedicalGuide).description)
                          //  Log.d("PRESENTATIONACTIVITY","LETRA: " + itemEstablishment.qualifications.get(itMedicalGuide).initial)
                            Log.d("PRESENTATIONACTIVITY","-----------------------------------")

                            establishmentList.add(itemEstablishment)

                            Log.d("PRESENTATIONACTIVITY", "PRESENTATIONESTABLISHMNET FOREACH Establishment.nome: "+ establishmentList.get(i).name )

                            i++
                        }
                    }

                }
            }
        }
        Log.d("PRESENTATIONACTIVITY","-----------VVVVVVVVVVV------------------------")
        for (itList in establishmentList){


         //   Log.d("PRESENTATIONACTIVITY", "Lista de medico "+ itList.name  )
            Log.d("PRESENTATIONACTIVITY", "Lista de medico "+ itList.name )


        }

  /*      for (i in 0..14){
            Log.d("PRESENTATIONACTIVITY", "Lista de medico FOR 2 "+ establishmentList.get(i).name )



        }*/
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
    /*    Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        Log.d("PRESENTATIONACTIVITY", "TOSTRING DA LISTA"+ establishmentList.toString() )
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")*/


      //  adapter.setEstablishments(presentationMedicalGuideList.establishments.toMutableList())
        adapter.setEstablishments(establishmentList)
        adapter.setQualificationsList(presentationMedicalGuideList.qualifications.toMutableList())
        adapter.setMedicalGuideListPlansV4(presentationMedicalGuideList.medicalGuideListPlansV4.toMutableList())
    }

    override fun onItemClick(establishment: PresentationEstablishment) {
        presenter.onItemClick(establishment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_medical_guide_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.action_map -> {
                    presenter.onMapClicked()
                    return true
                }
                else -> {
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showDialogError(it: Throwable) {
        val listener = { getMedicalGuideOptions() }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showLoading() {
        login_progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        login_progressbar.visibility = View.GONE
    }

    override fun renderFiltersText(plan: String, city: String, speciality: String) {
      /* plan_filter_text_view.text = plan
        city_filter_text_view.text = city
        speciality_filter_text_view.text = speciality*/
    }

    override fun showEmptyEstablishments() {
        DialogHelper.showDialog(context = this,
                title = R.string.title_medical_guide,
                message = R.string.text_empty_medical_guide,
                messagePositiveButton = R.string.text_ok,
                listenerPositiveButton = { presenter.onEmptyDialogOkClicked() },
                onDismiss = { presenter.onEmptyDialogOkClicked() })
    }

    override fun closeView() {
        finish()
    }
}