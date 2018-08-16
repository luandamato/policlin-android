package br.com.data.datasource.preferences

import io.reactivex.Completable
import io.reactivex.Flowable

interface PreferencesDatasource {

    fun saveToken(token: String): Completable

    fun getToken(): Flowable<String>

    fun putBoolean(key: String, value: Boolean): Completable

    fun getBoolean(key: String, defaultValue: Boolean): Flowable<Boolean>

    fun removeToken(): Completable
}