package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonUserResponse(@SerializedName("nome") val name: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("contrato") val contract: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("cpf") val cpf: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("dtaNasc") val birthday: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("ddd") val codeArea: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("celular") val phone: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("email") val email: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("validadeCarterinha") val registrationDate: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("codigoPlano") val codePlan: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("descricaoPlano") val descriptionPlan: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("matricula") val register: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("ordem") val order: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("foto_Perfil") val photo: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("msgInterna") val msgInternal: String?
                            = InvalidData.UNINITIALIZED.getString(),
                            @SerializedName("msgExterna") val msgExternal: String?
                            = InvalidData.UNINITIALIZED.getString())