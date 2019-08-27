package com.example.nghiamvvm.data

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import com.example.nghiamvvm.models.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
protected constructor() {

    val asObservable: Observable<Resource<ResultType>>

    init {
        val source: Observable<Resource<ResultType>>
        if (shouldFetch()) {
            source = createCall()
                .subscribeOn(Schedulers.io())
                .doOnNext { apiResponse -> saveCallResult(processResponse(apiResponse)) }
                .flatMap<Resource<ResultType>> { apiResponse ->
                    loadFromDb().toObservable().map { t: ResultType -> Resource.success(t) }
                }
                .doOnError { t -> onFetchFailed() }
                .onErrorResumeNext { error: Throwable ->
                     loadFromDb()
                        .toObservable()
                        .map { data -> Resource.error(error.message ?: "", data) }
                }
                .observeOn(AndroidSchedulers.mainThread())
        } else {
            source = loadFromDb()
                .toObservable()
                .map { type: ResultType -> Resource.success(type) }
        }

        asObservable = Observable.concat(
            loadFromDb()
                .toObservable()
                .map { type: ResultType -> Resource.loading(type) }
                .take(1),
            source
        )
    }

    protected fun onFetchFailed() {}

    @WorkerThread
    protected fun processResponse(response: Resource<RequestType>): RequestType {
        return response.data
    }

    @WorkerThread
    protected abstract fun saveCallResult(@NonNull item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(): Boolean

    @NonNull
    @MainThread
    protected abstract fun loadFromDb(): Flowable<ResultType>

    @NonNull
    @MainThread
    protected abstract fun createCall(): Observable<Resource<RequestType>>
}