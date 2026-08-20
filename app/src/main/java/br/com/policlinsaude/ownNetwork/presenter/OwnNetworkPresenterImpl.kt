package br.com.policlinsaude.ownNetwork.presenter


import android.util.Log
import br.com.domain.usecase.GetOwnNetworkUseCase
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationMedicalGuideListMapper
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.ownNetwork.navigator.OwnNetworkNavigator
import br.com.policlinsaude.ownNetwork.view.OwnNetworkView
import io.reactivex.rxkotlin.subscribeBy

class OwnNetworkPresenterImpl(private val getOwnNetworkUseCase: GetOwnNetworkUseCase,
                              private val view: OwnNetworkView,
                              private val navigator: OwnNetworkNavigator) : OwnNetworkPresenter {

    private var establishments: MutableList<Pair<String, List<PresentationEstablishment>>>? = null
    private lateinit var qualifications: MutableList<PresentationQualification>

    override fun getOwnNetworks() {
        UseCaseHandler.execute(getOwnNetworkUseCase)
                .doOnSubscribe {
                    Log.d("REDEPROPRIA","ANTES DO SHOWLOADING" )
                    view.showLoading()
                }
                .doOnTerminate {
                    Log.d("REDEPROPRIA","ANTES DO HIDELOADING" )
                    view.hideLoading()
                }
                .map {
                    Log.d("REDEPROPRIA","DENTRO DO PRIMEIRO MAP" )

                    qualifications = mutableListOf()
                    qualifications.addAll(PresentationMedicalGuideListMapper.transform(it.qualifications.toTypedArray()))

                    val list: MutableList<Pair<String, List<PresentationEstablishment>>> = mutableListOf()
                    it.establishments.forEach { t ->
                        val converted = PresentationMedicalGuideListMapper.transform(t.second.toTypedArray())
                        list.add(Pair(t.first, converted))
                    }
                    list
                }
                .map {
                    // todo should i remove this?
                    Log.d("REDEPROPRIA","DENTRO DO SEGUNDO MAP" )
                    val list: MutableList<Pair<String, List<PresentationEstablishment>>> = mutableListOf()
                    list.addAll(it)
                    Log.d("REDEPROPRIA","JSON UNIDADES NO PRESENTER em LISTA: " + list.toString())
                    for (i in 0 until it.size) {
                        if (it[i].first.equals("São José dos Campos", true)) {
                            list.removeAt(i)
                            list.add(0, it[i])
                            break
                        }
                    }
                    list
                }
                .subscribeBy(
                        onNext = {
                            establishments = it
                            Log.d("REDEPROPRIA","JSON UNIDADES NO PRESENTER: " + it.toString())
                            view.showOwnNetworks(it, qualifications)
                        },
                        onError = {
                            it.printStackTrace()
                            Log.d("REDEPROPRIA","DEU ERRO!!: " + it.cause)
                            view.showDialogError(it)
                        }
                )
    }

    override fun onMapClicked(selectedTabIndex: Int) {
        establishments?.forEachIndexed { index, pair ->
            if (selectedTabIndex == index) {
                navigator.goToMaps(ArrayList(pair.second))
            }
        }
    }
}