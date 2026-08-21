package br.com.policlinsaude.units.presenter


import android.util.Log
import br.com.policlinsaude.domain.usecase.GetUnitsUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.units.navigator.UnitsNavigator
import br.com.policlinsaude.units.view.UnitsView

import io.reactivex.rxkotlin.subscribeBy

class UnitsPresenterImpl(private val getUnitsUseCase: GetUnitsUseCase,
                         private val view: UnitsView,
                         private val navigator: UnitsNavigator) : UnitsPresenter {

   // private var establishments: MutableList<Pair<String, List<PresentationEstablishment>>>? = null
   private var establishments: MutableList<PresentationEstablishment>? = null

    private lateinit var qualifications: MutableList<PresentationQualification>

    //Andre ---
    override fun onItemClick(establishment: PresentationEstablishment) {
        navigator.goToDetails(establishment)
    }

    override fun getUnits() {
        Log.d("UNIDADES","INICIO de GETUNITS em UnitsPresenterImpl" )

        UseCaseHandler.execute(getUnitsUseCase)
                .doOnSubscribe {
                    Log.d("UNIDADES","ANTES DO SHOWLOADING" )
                    view.showLoading()
                }
                .doOnTerminate {
                    Log.d("UNIDADES","ANTES DO HIDELOADING" )
                    view.hideLoading()
                }
                .map {
                    Log.d("UNIDADES","DENTRO DO PRIMEIRO MAP" )

                    qualifications = mutableListOf()
                    qualifications.addAll(PresentationMedicalGuideListMapper.transform(it.qualifications.toTypedArray()))

                   // val list: MutableList<Pair<String, List<PresentationEstablishment>>> = mutableListOf()
             //       val list: MutableList<PresentationEstablishment> = mutableListOf()
                    establishments = mutableListOf()
                    establishments!!.addAll(PresentationMedicalGuideListMapper.transform(it.establishments.toTypedArray()))


                /*    it.establishments.forEach { t ->
                       // val converted = PresentationMedicalGuideListMapper.transform(t.second.toTypedArray())
                       // list.add(Pair(t.first, converted))
                        list.add(t)


                    }
                    list*/
                }
               /* .map {
                    // todo should i remove this?
                  //  val list: MutableList<Pair<String, List<PresentationEstablishment>>> = mutableListOf()
                    list.addAll(it)
                    Log.d("UNIDADES","JSON UNIDADES NO PRESENTER em LISTA: " + list.toString())
                    for (i in 0 until it.size) {
                        if (it[i].first.equals("São José dos Campos", true)) {
                            list.removeAt(i)
                            list.add(0, it[i])
                            break
                        }
                    }
                    list
                }*/
                .subscribeBy(
                        onNext = {
                        //    establishments = it //Lista
                            Log.d("UNIDADES","JSON UNIDADES NO PRESENTER: " + it.toString())
                           // view.showUnits(it, qualifications)
                            view.showUnits(establishments, qualifications)
                        },
                        onError = {
                            it.printStackTrace()
                            Log.d("UNIDADES","DEU ERRO!!: " + it.cause)
                            view.showDialogError(it)
                        }
                )
    }




    override fun onMapClicked(selectedTabIndex: Int) {
     /*   establishments?.forEachIndexed { index, pair ->
            if (selectedTabIndex == index) {
                navigator.goToMaps(ArrayList(pair.second))
            }
        }*/
    }
}