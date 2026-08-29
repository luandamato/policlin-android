package br.com.policlinsaude.data.local

import android.content.Context
import android.content.SharedPreferences
import br.com.policlinsaude.domain.models.HealthInsurancePhoto
import br.com.policlinsaude.domain.models.HealthInsurancePhotoList
import br.com.policlinsaude.domain.models.Person
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Persistência local de sessão (token, usuário, carteirinha e preferências).
 *
 * Baseada no antigo [PreferencesDatasourceImpl] e nos helpers de carteirinha
 * (photosPref/photoVersoPref). Não usa banco de dados local — apenas SharedPreferences.
 */
class SessionManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // =====================================================================
    // TOKEN
    // =====================================================================
    fun saveToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String = preferences.getString(KEY_TOKEN, "").orEmpty()

    fun removeToken() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }

    fun hasToken(): Boolean = getToken().isNotEmpty()

    // =====================================================================
    // USUÁRIO (Pessoa logada)
    // =====================================================================
    fun savePerson(person: Person) {
        preferences.edit().putString(KEY_PERSON, gson.toJson(person)).apply()
    }

    fun getPerson(): Person? {
        val json = preferences.getString(KEY_PERSON, null) ?: return null
        return try {
            gson.fromJson(json, Person::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun removePerson() {
        preferences.edit().remove(KEY_PERSON).apply()
    }

    fun hasPerson(): Boolean = getPerson() != null

    // =====================================================================
    // CARTEIRINHA (fotos frente/verso)
    // =====================================================================
    fun saveHealthInsurancePhotos(photos: List<HealthInsurancePhoto>) {
        preferences.edit().putString(KEY_PHOTOS, gson.toJson(photos)).apply()
    }

    fun getHealthInsurancePhotos(): List<HealthInsurancePhoto> {
        val json = preferences.getString(KEY_PHOTOS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<HealthInsurancePhoto>>() {}.type
            gson.fromJson<List<HealthInsurancePhoto>>(json, type).orEmpty()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveHealthInsuranceVerso(photo: String) {
        preferences.edit().putString(KEY_PHOTO_VERSO, photo).apply()
    }

    fun getHealthInsuranceVerso(): String = preferences.getString(KEY_PHOTO_VERSO, "").orEmpty()

    fun saveCarteirinha(photoList: HealthInsurancePhotoList) {
        saveHealthInsurancePhotos(photoList.listaimgFrente.orEmpty())
        photoList.imgVerso?.let { saveHealthInsuranceVerso(it) }
    }

    fun clearCarteirinha() {
        preferences.edit()
            .remove(KEY_PHOTOS)
            .remove(KEY_PHOTO_VERSO)
            .apply()
    }

    // =====================================================================
    // PREFERÊNCIAS BOOLEANAS
    // =====================================================================
    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)

    fun getSelecaoBeneficiarioAtiva(): Boolean =
        preferences.getBoolean(KEY_SELECAO_BENEFICIARIO, false)

    // =====================================================================
    // CLEAR TOTAL (logoff)
    // =====================================================================
    fun clearSession() {
        removeToken()
        removePerson()
        clearCarteirinha()
    }

    private companion object {
        const val PREFERENCES_NAME = "POLICLIN_SAUDE"
        const val KEY_TOKEN = "token"
        const val KEY_PERSON = "person"
        const val KEY_PHOTOS = "photosPref"
        const val KEY_PHOTO_VERSO = "photoVersoPref"
        const val KEY_SELECAO_BENEFICIARIO = "selecaoBeneficiarioAutorizador"
    }
}
