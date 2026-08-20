package com.policlinsaude.newfeature.data.networking

data class ViewModelResponse<T, E: Exception> (
    private var responseStatus: ViewModelResponseStatus = ViewModelResponseStatus.RUNNING,
    private var data: T? = null,
    private var error: E? = null
) {
    fun setData(t: T): ViewModelResponse<T, E> {
        data = t
        responseStatus = ViewModelResponseStatus.SUCCESS
        return this
    }

    fun setError(error: E): ViewModelResponse<T, E> {
        this.error = error
        responseStatus = ViewModelResponseStatus.FAILED
        return this
    }

    fun setEmpty() {
        responseStatus = ViewModelResponseStatus.EMPTY
        data = null
        error = null
    }

    fun setRunning(): ViewModelResponse<T, E> {
        responseStatus = ViewModelResponseStatus.RUNNING
        return this
    }

    fun getError(): E? = error

    fun getResponseStatus(): ViewModelResponseStatus = responseStatus

    fun getData(): T? {
        responseStatus = ViewModelResponseStatus.EMPTY
        error = null
        return data
    }
}