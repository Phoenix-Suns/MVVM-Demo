package com.example.nghiamvvm.models

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class Resource<T> private constructor(
    @param:NonNull
    val status: Status?, @param:Nullable
    val data: T, @param:Nullable val message: String?
) {

    val isSuccess: Boolean
        get() = status === Status.SUCCESS && data != null

    val isLoading: Boolean
        get() = status === Status.LOADING

    val isLoaded: Boolean
        get() = status !== Status.LOADING

    companion object {

        fun <T> success(@NonNull data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, @Nullable data: T): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(@Nullable data: T): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}