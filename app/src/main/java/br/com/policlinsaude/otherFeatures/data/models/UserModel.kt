package com.policlinsaude.newfeature.data.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("nome") val name: String? = "",
    @SerializedName("contrato") val contract: String? = "",
    @SerializedName("cpf") val cpf: String? = "",
    @SerializedName("dtaNasc") val birthday: String? = "",
    @SerializedName("ddd") val codeArea: String? = "",
    @SerializedName("celular") val phone: String? = "",
    @SerializedName("email") val email: String? = "",
    @SerializedName("validadeCarterinha") val registrationDate: String? = "",
    @SerializedName("codigoPlano") val codePlan: String? = "",
    @SerializedName("descricaoPlano") val descriptionPlan: String? = "",
    @SerializedName("matricula") val register: String? = "",
    @SerializedName("ordem") val order: String? = "",
    @SerializedName("foto_Perfil") val photo: String? = "",
    @SerializedName("msgInterna") val msgInternal: String? = "",
    @SerializedName("msgExterna") val msgExternal: String? = ""
)