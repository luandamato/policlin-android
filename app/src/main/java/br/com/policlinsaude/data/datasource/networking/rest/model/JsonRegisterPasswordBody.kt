package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonRegisterPasswordBody(
        @SerializedName("matricula") var register: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("ordem") var order: String = InvalidData.UNINITIALIZED.getString(),
        var cpf: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("contrato") var contract: String = InvalidData.UNINITIALIZED.getString(),
        var email: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("nome") var name: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("ddd") var codeAreaPhone: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("celular") var phone: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("validadeCarterinha") var validationRegistration: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("senha") var password: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("aceiteTermo") var acceptTerm: Int = InvalidData.UNINITIALIZED.getInt(),
        @SerializedName("dtaNasc") var birthday: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("avatar") var avatar: String = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("nomeMae") var mothersName: String = InvalidData.UNINITIALIZED.getString())