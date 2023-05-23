package br.com.data.datasource.preferences

import android.content.Context
import android.content.SharedPreferences
import br.com.data.exception.PreferenceNotFoundException
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class PreferencesDatasourceImpl(context: Context) : PreferencesDatasource {

    companion object {
        private const val PREFERENCES_NAME = "POLICLIN_SAUDE"
        private const val PREFERENCES_TOKEN = "token"
    }

    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    }

    override fun saveToken(token: String): Completable {
        val edit = preferences.edit()
        edit.putString(PREFERENCES_TOKEN, token)
        edit.apply()

        return Completable.complete()
    }

    override fun getToken(): Flowable<String> {
        val token = preferences.getString(PREFERENCES_TOKEN, "")
        return if (token.isNullOrEmpty()) Flowable.error<String>(PreferenceNotFoundException("error found token")) else Flowable.just(token)
    }

    override fun putBoolean(key: String, value: Boolean): Completable {
        return Completable.create { e ->
            if (key.isEmpty()) {
                e.onError(Throwable("Preference key must not be null"))
            }
            val edit = preferences.edit()
            edit.putBoolean(key, value)
            edit.apply()

            e.onComplete()
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
        return Completable.create { e ->
            val token = preferences.getString(PREFERENCES_TOKEN, "")
            if (token.isNullOrEmpty()) e.onComplete()

            val edit = preferences.edit()
            edit.putString(PREFERENCES_TOKEN, "")
            edit.apply()

            e.onComplete()
        }
    }
}