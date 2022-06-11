package br.com.policlinsaude.medicalGuideList.presenter

import android.util.Log
import br.com.domain.usecase.GetMedicalGuideListUseCase
import br.com.domain.usecase.requestvalues.MedicalGuideListRV
import br.com.policlinsaude.core.helper.InvalidData
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import br.com.policlinsaude.medicalGuideList.navigator.MedicalGuideListNavigator
import br.com.policlinsaude.medicalGuideList.view.MedicalGuideListView
import br.com.policlinsaude.medicalGuideOptions.presenter.model.PresentationLocation
import br.com.policlinsaude.model.*
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.view_filters.*

class MedicalGuideListPresenterImpl(private val navigator: MedicalGuideListNavigator,
                                    private val getMedicalGuideListUseCase: GetMedicalGuideListUseCase,
                                    private val view: MedicalGuideListView) : MedicalGuideListPresenter {

    private var establishments: List<PresentationEstablishment>? = null
    //Andre
    private var medicalGuideListPlansV4: List<PresentationMedicalGuideListPlansV4>? = null
    //Andre ---
    override fun onItemClick(establishment: PresentationEstablishment) {
        navigator.goToDetails(establishment)
    }

    override fun getMedicalGuideList(presentationPlanOptions: PresentationPlanOptions,
                                     presentationCityOptions: PresentationCityOptions,
                                     presentationSpecialityServiceOptions: PresentationSpecialityServiceOptions,
                                     presentationProfessionalClass: PresentationProfessionalClass,
                                     presentationServiceType: PresentationServiceType, presentationEstablishmentType: PresentationEstablishmentType,
                                     address_filter: String, neighborhood_filter: String, zipcode_filter: String, number_on_the_board_filter: String,//Andre
                                     prof_fantasy_filter: String, cnpj_filter: String, phones_filter: String, qualificationsSearch: String?, //Andre
                                     presentationLocation: PresentationLocation?) {

        Log.d("PRESENTER","Phones: " + phones_filter)
        Log.d("PRESENTER","Nome  : " + prof_fantasy_filter)


        view.renderFiltersText(presentationPlanOptions.description, presentationCityOptions.description, presentationSpecialityServiceOptions.description)
        val getMedicalGuideListRv = MedicalGuideListRV(codePlan = presentationPlanOptions.codePlan,
                codeCity = presentationCityOptions.code,
                codeSpecialityService = presentationSpecialityServiceOptions.code,
                latitude = presentationLocation?.latitude ?: InvalidData.UNINITIALIZED.getDouble(),
                longitude = presentationLocation?.longitude ?: InvalidData.UNINITIALIZED.getDouble(),
                codeProfessionalClass = presentationProfessionalClass.codeProfessionalClass,
                codeServiceType = presentationServiceType.codeServiceType,
                codeEstablishmentType = presentationEstablishmentType.codeEstablishmentType,
                specialityType = presentationSpecialityServiceOptions.type,
                address_filter = address_filter,
                neighborhood_filter = neighborhood_filter,
                zipcode_filter = zipcode_filter,
                number_on_the_board_filter = number_on_the_board_filter,
                prof_fantasy_filter = prof_fantasy_filter,
                cnpj_filter = cnpj_filter,
                phones_filter = phones_filter,
                qualificationsSearch = qualificationsSearch)
        
        UseCaseHandler.execute(getMedicalGuideListUseCase, getMedicalGuideListRv)
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .map(PresentationMedicalGuideListMapper::transform)
                .subscribeBy(
                        onNext = {
                         //   if (it.establishments.isEmpty()) {
                            if (it.medicalGuideListPlansV4.isEmpty()) {
                                view.showEmptyEstablishments()
                           } else {
                               // this.establishments = it.establishments
                                this.medicalGuideListPlansV4 = it.medicalGuideListPlansV4

                                view.showMedicalGuideList(it)
                            }
                        },
                        onError = {
                            view.showDialogError(it)
                        }
                )
    }

    override fun onMapClicked() {
        Log.d("PRESENTATION","ONMAPCLICKED!!!!!!!-------------------")

        var listPlansV4List: List<PresentationMedicalGuideListPlansV4>?
        //var establishmentList: List<PresentationEstablishment>? = null

        var establishmentList: MutableList<PresentationEstablishment> = ArrayList()

        var itemEstablishment: PresentationEstablishment
        var citiesList: List <PresentationMedicalGuideListCitiesV4>
        var serviceTypesV4List: List<PresentationMedicalGuideListServiceTypesV4>
        var especialityList: List<PresentationMedicalGuideListSpecialitiesV4>
        var medicalGuideV4: List<PresentationEstablishment>


        if (medicalGuideListPlansV4 != null) {
        listPlansV4List = medicalGuideListPlansV4!!.toMutableList()

        var flagPlan = false


        for (itPlan in 0..listPlansV4List.size-1) {
            //for (item in listPlansV4List){

            if (!flagPlan)
                flagPlan = true


            var flagCity = false

            //  citiesList = item.cities.toMutableList()
            citiesList = listPlansV4List.get(itPlan).cities.toMutableList()

            for (itCity in 0..citiesList.size - 1) {

                if (!flagCity)
                    flagCity = true


                serviceTypesV4List = citiesList.get(itCity).serviceType.toMutableList()


                var flagServiceType = false


                //for (serviceType in serviceTypesV4List) {
                for (itServiceType in 0..serviceTypesV4List.size - 1) {
                    //   itemEstablishment?.serviceTypeV4 = serviceType.serviceType

                    if (!flagServiceType)
                        flagServiceType = true

                    var serviceTypeRef: Int = itServiceType
                    //especialityList = serviceType.specialities.toMutableList()
                    especialityList = serviceTypesV4List.get(itServiceType).specialities.toMutableList()


                    var flagEspeciality = false

                    for (itEspeciality in 0..especialityList.size - 1) {
                        //for (especiality in especialityList) {

                        if (!flagEspeciality)
                            flagEspeciality = true

                        //medicalGuideV4 = especiality.medicalGuide.toMutableList()
                        medicalGuideV4 = especialityList.get(itEspeciality).medicalGuide.toMutableList()

                        var i: Int = 0

                        for (itMedicalGuide in 0..medicalGuideV4.size - 1) {
                            //for (medicalGuide in medicalGuideV4){


                            itemEstablishment = PresentationEstablishment()

                            itemEstablishment.planNameV4 = listPlansV4List.get(itPlan).planName
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



                            Log.d("PRESENTATIONACTIVITY", "-----------------------------------")
                            Log.d("PRESENTATIONACTIVITY", "itMedicalGuide TOSTRING; " + medicalGuideV4.get(itMedicalGuide).qualifications.toString())
                            //  Log.d("PRESENTATIONACTIVITY","DESCRICAO: " + itemEstablishment.qualifications.get(itMedicalGuide).description)
                            //  Log.d("PRESENTATIONACTIVITY","LETRA: " + itemEstablishment.qualifications.get(itMedicalGuide).initial)
                            Log.d("PRESENTATIONACTIVITY", "-----------------------------------")

                            establishmentList.add(itemEstablishment)

                            Log.d("PRESENTATIONACTIVITY", "PRESENTATIONESTABLISHMNET FOREACH Establishment.nome: " + establishmentList.get(i).name)

                            i++
                        }
                    }

                }
            }
         }
        }
        //establishments?.let {
        establishmentList?.let {
            navigator.goToMaps(ArrayList(it))
        }
    }

    override fun onEmptyDialogOkClicked() {
        view.closeView()
    }
}