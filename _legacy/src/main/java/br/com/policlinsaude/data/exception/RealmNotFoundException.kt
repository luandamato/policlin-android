package br.com.policlinsaude.data.exception

/**
 * Created by lmiyagi on 3/22/18.
 */
class RealmNotFoundException : Exception {

    constructor(typeOfClass: Class<*>)
            : super("No data was found for ${typeOfClass.name}")
}
