package br.com.policlinsaude.notHasPassword.presenter

import br.com.domain.usecase.CheckPlanUseCase
import br.com.domain.usecase.RegisterPasswordUseCase
import br.com.domain.usecase.requestvalues.CheckPlanRV
import br.com.domain.usecase.requestvalues.RegisterPasswordRV
import br.com.policlinsaude.core.helper.UseCaseHandler
import br.com.policlinsaude.mapper.PresentationPersonMapper
import br.com.policlinsaude.mapper.RegisterPasswordMapper
import br.com.policlinsaude.model.PresentationPerson
import br.com.policlinsaude.model.PresentationPlan
import br.com.policlinsaude.notHasPassword.navigator.NotHasPasswordNavigator
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordView
import io.reactivex.rxkotlin.subscribeBy

class NotHasPasswordPresenterImpl(private val navigator: NotHasPasswordNavigator,
                                  private val registerPasswordUseCase: RegisterPasswordUseCase,
                                  private val checkPlanUseCase: CheckPlanUseCase,
                                  private val view: NotHasPasswordView) : NotHasPasswordPresenter {

    private var presentationPerson: PresentationPerson = PresentationPerson()
    private var presentationPlan: PresentationPlan = PresentationPlan()

    override fun clickedButtonComplete() {
        val mapper = RegisterPasswordMapper.transform(presentationPerson, presentationPlan)
        val registerPasswordRV = RegisterPasswordRV(person = mapper.first, plan = mapper.second)
        UseCaseHandler.execute(registerPasswordUseCase, registerPasswordRV)
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .subscribeBy(
                        onNext = {
                          view.showSuccessDialog(it)
                        },
                        onError = {
                            view.showDialogError(it)
                        }
                )
    }

    override fun setPresentationPerson(presentationPerson: PresentationPerson) {
        this.presentationPerson = presentationPerson
    }

    override fun getPresentationPlan(): PresentationPlan = presentationPlan

    override fun getPresentationPerson(): PresentationPerson = presentationPerson

    override fun checkPlan(presentationPlan: PresentationPlan, onResult: (Boolean) -> Unit) {
        UseCaseHandler.execute(checkPlanUseCase, CheckPlanRV(PresentationPersonMapper.transform(presentationPerson),
                RegisterPasswordMapper.transform(presentationPlan)))
                .doOnSubscribe {
                    view.showLoading()
                }
                .doOnTerminate {
                    view.hideLoading()
                }
                .subscribeBy(
                        onComplete = {
                            onResult(true)
                        },
                        onError = {
                            it.printStackTrace()
                            view.showDialogError(it)
                            onResult(false)
                        })
    }

    override fun onImagePicked(image: String?) {
        if (image.isNullOrEmpty()) {
            view.showImagePickError()
        } else {
            presentationPerson.photo = image!!
        }
    }

    override fun onImagePickError() {
        view.showImagePickError()
    }

    override fun onSuccessDialogDismissed() {
        navigator.finishScreen()
    }
}