package com.example.task.presentation.utils

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {

        fun <T> Success(freshOrOld: String,data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                freshOrOld
            )
        }

        fun <T> Error(msg: String, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                msg
            )
        }

        fun <T> Loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }
}