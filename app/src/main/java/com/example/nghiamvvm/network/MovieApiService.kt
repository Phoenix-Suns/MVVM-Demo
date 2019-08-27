package com.example.nghiamvvm.network

import io.reactivex.Observable
import retrofit2.http.GET

interface MovieApiService {

    @GET("movie/popular?language=en-US&region=US&page=1")
    fun fetchMoviesByType(): Observable<MovieApiResponse>
}