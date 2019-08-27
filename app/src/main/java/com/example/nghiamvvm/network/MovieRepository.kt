package com.example.nghiamvvm.network

import com.example.nghiamvvm.data.MovieDao
import com.example.nghiamvvm.data.MovieEntity
import com.example.nghiamvvm.data.NetworkBoundResource
import com.example.nghiamvvm.models.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class MovieRepository(
    private val movieDao: MovieDao,
    private val movieApiService: MovieApiService
) {

    fun loadMoviesByType(): Observable<Resource<List<MovieEntity>>> {
        return object: NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            override fun saveCallResult(item: MovieApiResponse) {
                movieDao.insertMovies(item.results)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<MovieEntity>> {
                val movieEntities = movieDao.getMoviesByPage()
                return if (movieEntities == null || movieEntities.isEmpty()) {
                    Flowable.empty()
                } else Flowable.just(movieEntities)
            }

            override fun createCall(): Observable<Resource<MovieApiResponse>> {
                return movieApiService.fetchMoviesByType()
                    .flatMap { movieApiResponse ->
                        Observable.just(
                            if (movieApiResponse == null) Resource.error("", MovieApiResponse(1, emptyList(), 0, 1))
                            else Resource.success(movieApiResponse)
                        )
                    }
            }
        }.asObservable
    }
}