package br.com.data.datasource.networking.rest

import br.com.data.BuildConfig
import br.com.data.datasource.networking.rest.model.*
import br.com.data.di.NetworkingModule
import br.com.data.helper.InvalidData
import br.com.policlinsaude.domain.model.UserConnected
import br.com.policlinsaude.domain.model.ValidateButtonBody
import br.com.policlinsaude.domain.model.ValidateButtons
import io.reactivex.Flowable
import retrofit2.http.*

interface NetworkingService {

    @FormUrlEncoded
    @POST("MAPP_Login")
    fun login(@Field("matricula") register: String = InvalidData.UNINITIALIZED.getString(),
              @Field("ordem") order: String = InvalidData.UNINITIALIZED.getString(),
              @Field("senha") password: String = InvalidData.UNINITIALIZED.getString(),
              @Field("firebaseToken") firebaseToken: String = InvalidData.UNINITIALIZED.getString(),
              @Header("verOS") osVersion: String = "13",
              @Header("plataforma") plataforma: String = "A",
              @Header("versao") versao: String = BuildConfig.VERSION_NAME
    ): Flowable<JsonLoginResponse>


    @FormUrlEncoded
    @POST("MAPP_RecuperarSenha")
    fun recoverPassword(@Field("matricula") register: String = InvalidData.UNINITIALIZED.getString(),
                        @Field("ordem") order: String = InvalidData.UNINITIALIZED.getString(),
                        @Field("email") email: String = InvalidData.UNINITIALIZED.getString()
    ): Flowable<JsonRecoverPasswordResponse>

    @POST("MAPP_ManutencaoBeneficiario")
    fun registerPassword(@Body json: JsonRegisterPasswordBody): Flowable<JsonRecoverPasswordResponse>

    @FormUrlEncoded
    @POST("MAPP_CarterinhaVirtualv2")
    fun getHealthInsurancePhoto(@Field("token") token: String = InvalidData.UNINITIALIZED.getString(),
                                @Field("verificaAlteracao") checkUpdated: Int = InvalidData.UNINITIALIZED.getInt())
            : Flowable<JsonHealthInsurancePhotoResponse>

   //Andre
  //  @GET("MAPP_RetornaGuiaMedicoFiltro")
  //  fun getMedicalGuideOptions(): Flowable<JsonMedicalGuideOptionsResponse>

    //Andre
    @GET("MAPP_RetornaGuiaMedicoFiltrov2")

    fun getMedicalGuideOptions(): Flowable<JsonMedicalGuideOptionsResponse>


   @GET("MAPP_Retorna_GuiaMedicov4")
//    @GET("MAPP_Retorna_GuiaMedico")
    fun getMedicalGuideList(@Query("Codigo_Plano") codePlan: Int = InvalidData.UNINITIALIZED.getInt(),
                            @Query("Codigo_Cidade") codeCity: Int = InvalidData.UNINITIALIZED.getInt(),
                            @Query("Codigo_Especialidade_Servico") codeSpecialityService: Int = InvalidData.UNINITIALIZED.getInt(),
                            @Query("RedePropria") ownNetwork: Int = InvalidData.UNINITIALIZED.getInt(),
                            @Query("locLatitude") latitude: Double? = null,
                            @Query("locLongitude") longitude: Double? = null,
                            @Query("FilAvanc_ClasseProfissional") professionalClassOption: String? = null,
                            @Query("FilAvanc_TipoServico") serviceTypeOption: String? = null,
                            @Query("FilAvanc_TipoEstabelecimentoCNES") establishmentType: String? = null,
                            @Query("Ent_Tipo") specialityType:String? = null,
                            @Query("FilAvanc_Endereco") address_filter: String? = null,
                            @Query("FilAvanc_Bairro") neighborhood_filter: String? = null,
                            @Query("FilAvanc_CEP") zipcode_filter: String? = null,
                            @Query("FilAvanc_NumeroConselho") number_on_the_board_filter: String? = null,
                            @Query("FilAvanc_NomeProfissional_NomeFantasia_RazaoSocial") prof_fantasy_fliter: String? = null,
                            @Query("FilAvanc_CNPJ") cnpj_filter: String? = null,
                            @Query("FilAvanc_Telefone") phones_filter: String? = null,
                            @Query("FilAvanc_Qualificacoes") qualificationsSearch: String? = null,
                            @Query("token") token: String? = null): Flowable<JsonMedicalGuideListResponse>



    @GET("MAPP_RetornaRedePropria")
    fun getOwnNetwork(@Query("token") token: String? = null): Flowable<JsonOwnNetworkResponse>

    @GET("MAPP_RetornaGuiaMedicoDetalhes")
    fun getMedicalGuideDetails(@Query("PROUF") proUF: String = InvalidData.UNINITIALIZED.getString(),
                               @Query("PRSCOD") prsCod: String = InvalidData.UNINITIALIZED.getString(),
                               @Query("PROCLS") proCls: String = InvalidData.UNINITIALIZED.getString(),
                               @Query("PROCOD") proCod: String = InvalidData.UNINITIALIZED.getString()): Flowable<JsonMedicalGuidePlansResponse>

    @FormUrlEncoded
    @POST("MAPP_ManutencaoFavoritos")
    fun addToFavorite(@Field("token") token: String,
                      @Field("metodo") method: String,
                      @Field("_PROCLS") proCls: String,
                      @Field("_PROCOD") proCod: String,
                      @Field("_PROUF") proUF: String,
                      @Field("_PRSCOD") prsCod: String,
                      @Field("_PRSSEQ") prsSeq: String,
                      @Field("_ESCOD") esCod: String,
                      @Field("_Tipo") type: String): Flowable<JsonFavoriteResponse>

    @FormUrlEncoded
    @POST("MAPP_RetornaFavoritos")
    fun getFavorites(@Field("token") token: String): Flowable<JsonFavoritesResponse>

    @FormUrlEncoded
    @POST("MAPP_AtualizaAvatar")
    fun updateAvatar(@Field("token") token: String,
                     @Field("avatar") imageString: String): Flowable<JsonUpdateAvatarResponse>

    @FormUrlEncoded
    @POST("MAPP_Sair")
    fun doLogoff(@Field("token") token: String): Flowable<JsonLogoffResponse>

    @FormUrlEncoded
    @POST("MAPP_AtualizaSenha")
    fun editPassword(@Field("token") token: String,
                     @Field("senha") password: String): Flowable<JsonEditPasswordResponse>

    @FormUrlEncoded
    @POST
    fun getBanners(@Url bannersUrl: String,
                   @Field("retornaFoto") returnImage: Boolean,
                   @Field("id") id: Int): Flowable<JsonGetBannersResponse>

    @FormUrlEncoded
    @POST("MAPP_AtualizaTelefone")
    fun editPhone(@Field("token") token: String,
                  @Field("DDD") codeArea: String,
                  @Field("Telefone") phone: String): Flowable<JsonEditPhoneResponse>

    @FormUrlEncoded
    @POST("MAPP_RetornaBeneficiario")
    fun getPerson(@Field("token") token: String,
                  @Field("verificaAlteracao") verifyChanges: Int): Flowable<JsonUserResponse>

    @FormUrlEncoded
    @POST("MAPP_ManutencaoBeneficiario")
    fun checkPlan(@Field("matricula") register: String,
                  @Field("ordem") order: String,
                  @Field("cpf") cpf: String,
                  @Field("contrato") contract: String,
                  @Field("email") email: String,
                  @Field("nome") name: String,
                  @Field("ddd") ddd: String,
                  @Field("celular") phone: String,
                  @Field("validadeCarterinha") expirationDate: String,
                  @Field("aceiteTermo") termAccepted: Int,
                  @Field("dtaNasc") birthday: String,
                  @Field("metodo") method: String,
                  @Field("nomeMae") mothersName: String
    ): Flowable<JsonCheckPlanResponse>

    @FormUrlEncoded
    @POST("MAPP_ManutencaoFavoritos")
    fun removeFromFavorites(@Field("token") token: String,
                            @Field("metodo") method: String,
                            @Field("_PROCLS") proCls: String,
                            @Field("_PROCOD") proCod: String,
                            @Field("_PROUF") proUF: String,
                            @Field("_PRSCOD") prsCod: String,
                            @Field("_PRSSEQ") prsSeq: String,
                            @Field("_ESCOD") esCod: String,
                            @Field("_Tipo") type: String): Flowable<JsonRemoveFromFavoritesResponse>

    @GET("MAPP_RetornaRedeUnidades")

  // @GET("MAPP_RetornaRedePropria")
    fun getUnits(@Query("token") token: String? = null): Flowable<JsonUnitsResponse>


    @FormUrlEncoded
    @POST("MAPP_ValidaBeneficiario")
    fun validateUserConnected(
        @Field("matricula") registration: String,
        @Field("ordem") order: String,
        @Field("token") token: String = InvalidData.UNINITIALIZED.getString(),
        @Header("verOS") osVersion: String = "13",
        @Header("plataforma") plataforma: String = "A",
        @Header("versao") versao: String = BuildConfig.VERSION_NAME
    ): Flowable<UserConnected>

    @POST("")
    fun validateButtons(
        @Url url: String = "http://policlinsaude.com.br/apiapp/rest/apiAcessoBotoes",
        @Body body: ValidateButtonBody
    ): Flowable<ValidateButtons>




}
