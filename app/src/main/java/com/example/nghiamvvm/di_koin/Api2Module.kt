package com.example.nghiamvvm.di_koin

import android.app.Application
import com.example.nghiamvvm.AppConstants
import com.example.nghiamvvm.models.RequestInterceptor
import com.example.nghiamvvm.network.MovieApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

val Api2Module = module {
    // get() là tự tìm fun trả về thêm vào
    // single: chỉ khởi tạo 1 lần
    single { provideGson() }
    single { provideCache(get()) }
    single { provideOkhttpClient(get()) }
    single { provideRetrofit(get(), get()) }
    single { provideMovieApiService(get())}
}

fun provideGson(): Gson {
    val gsonBuilder = GsonBuilder()
    return gsonBuilder.create()
}

fun provideCache(application: Application): Cache {
    val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
    val httpCacheDirectory = File(application.cacheDir, "http-cache")
    return Cache(httpCacheDirectory, cacheSize)
}

fun provideOkhttpClient(cache: Cache): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    val httpClient = OkHttpClient.Builder()
    httpClient.cache(cache)
    httpClient.addInterceptor(logging)
    httpClient.addNetworkInterceptor(RequestInterceptor())
    httpClient.connectTimeout(30, TimeUnit.SECONDS)
    httpClient.readTimeout(30, TimeUnit.SECONDS)
    return httpClient.build()
}

fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(AppConstants.BASE_URL)
        .client(okHttpClient)
        .build()
}

fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
    return retrofit.create(MovieApiService::class.java)
}