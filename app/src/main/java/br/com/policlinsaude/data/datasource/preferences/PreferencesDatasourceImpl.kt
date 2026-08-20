package br.com.policlinsaude.data.datasource.preferences

import android.content.Context
import android.content.SharedPreferences
import br.com.policlinsaude.data.exception.PreferenceNotFoundException
import br.com.policlinsaude.domain.model.Person
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class PreferencesDatasourceImpl(context: Context) : PreferencesDatasource {

    companion object {
        private const val PREFERENCES_NAME = "POLICLIN_SAUDE"
        private const val PREFERENCES_TOKEN = "token"
        private const val PREFERENCES_PERSON = "person"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun saveToken(token: String): Completable {
        return Completable.fromAction {
            preferences.edit().putString(PREFERENCES_TOKEN, token).apply()
        }
    }

    override fun getToken(): Flowable<String> {
        val token = preferences.getString(PREFERENCES_TOKEN, "")
        return if (token.isNullOrEmpty()) {
            Flowable.error(PreferenceNotFoundException("Token not found"))
        } else {
            Flowable.just(token)
        }
    }

    override fun putBoolean(key: String, value: Boolean): Completable {
        return Completable.fromAction {
            preferences.edit().putBoolean(key, value).apply()
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Flowable<Boolean> {
        return Flowable.create({ e ->
            try {
                val preference = preferences.getBoolean(key, defaultValue)
                e.onNext(preference)
            } catch (exception: Exception) {
                e.onError(exception)
            }
            e.onComplete()
        }, BackpressureStrategy.LATEST)
    }

    override fun removeToken(): Completable {
        return Completable.fromAction {
            preferences.edit().remove(PREFERENCES_TOKEN).apply()
        }
    }

    override fun savePerson(person: Person): Completable {
        return Completable.fromAction {
            val personJson = gson.toJson(person)
            preferences.edit().putString(PREFERENCES_PERSON, personJson).apply()
        }
    }

    override fun getPerson(): Flowable<Person> {
        val personJson = preferences.getString(PREFERENCES_PERSON, "")
        return if (personJson.isNullOrEmpty()) {
            Flowable.error(PreferenceNotFoundException("Person not found"))
        } else {
            try {
                val person = gson.fromJson(personJson, Person::class.java)
                Flowable.just(person)
            } catch (e: Exception) {
                Flowable.error(e)
            }
        }
    }

    override fun removePerson(): Completable {
        return Completable.fromAction {
            preferences.edit().remove(PREFERENCES_PERSON).apply()
        }
    }
}
