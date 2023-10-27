package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.RetrofitInstance
import com.policlinsaude.newfeature.data.networking.RetrofitInstance.Companion.API_NOTIFICATION
import com.policlinsaude.newfeature.data.services.GuideService
import com.policlinsaude.newfeature.data.services.TicketService
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosRequestModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosResponseModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.*
import com.policlinsaude.newfeature.utils.makeRequest
import kotlinx.coroutines.coroutineScope

class GuideRepositoryImpl: GuideRepository {

    override suspend fun onGetGuideAuthorizer(token: GuideAuthorizerRequestModel): GuideAuthorizerResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .getGuideAuthorizer(token)
            }
        }
    }

    override suspend fun onGetProfile(token: String, verify: Int): UserModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java, baseUrl = RetrofitInstance.API_NOTIFICATION)
                    .getPerson(token, verify)
            }
        }
    }

    override suspend fun onGetCities(body: CityRequestModel): CitiesResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .getCities(body)
            }
        }
    }

    override suspend fun onPostGuideAuthorizer(form: ProcessRequestModel): GuideAuthorizerRequestResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postGuideAuthorizer(form)
            }
        }
    }

    override suspend fun onPostGuideAuthorizerPhotos(form: ProcessRequestPhotosModel): ProcessResponsePhotosModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java, baseUrl = API_NOTIFICATION)
                    .postGuideAuthorizerPhotos(form)
            }
        }
    }

    override suspend fun onPostGuideAuthorizerCancel(cancel: GuideAuthorizerRequestCancelModel): GuideAuthorizerResponseCancelModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postGuideAuthorizerCancel(cancel)
            }
        }
    }

    override suspend fun onPostRequestDeadlines(token: String): ResponseDeadlineModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postRequestDeadline(CityRequestModel(token))
            }
        }
    }

    override suspend fun onPostRequestDetails(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerRequestResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postDetails(body)
            }
        }
    }

    override suspend fun onPostRequestPicturesDetails(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerResponsePicturesDetailsModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postGetPicturesDetails(body)
            }
        }
    }

    override suspend fun onPostRequestQuestions(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerQuestionsModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postGetQuestions(body)
            }
        }
    }

    override suspend fun onPostGuide(body: GuideAuthorizerRequestCancelModel): GuideModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .postGuide(body)
            }
        }
    }

    override suspend fun onPostSendAnswer(body: ProcessRequestSendAnswerModel): ComumModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java)
                    .sendAnswer(body)
            }
        }
    }

    override suspend fun onPostSendAnswerAttachment(body: ProcessRequestSendAnswerAttachmentModel): ComumModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(GuideService::class.java, baseUrl = API_NOTIFICATION)
                    .sendAnswerAttachment(body)
            }
        }
    }

    override suspend fun onGetDependents(token: BeneficiariosRequestModel): BeneficiariosResponseModel {
        return coroutineScope {
            makeRequest {
                RetrofitInstance().create(TicketService::class.java)
                    .onGetDependents(token)
            }
        }
    }


}