package br.com.policlinsaude.data.exception

/**
 * Created by lmiyagi on 3/23/18.
 */
class PreferenceNotFoundException(preference: String) : Exception("Preference not found: $preference") {
}