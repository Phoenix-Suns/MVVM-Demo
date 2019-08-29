package com.example.nghiamvvm.di_koin

import android.app.Application
import androidx.room.Room
import com.example.nghiamvvm.data.AppDatabase
import com.example.nghiamvvm.data.MovieDao
import org.koin.dsl.module

val Db2Module = module {
    single { provideDatabase(get()) }
    single { provideMovieDao(get())}
}

fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
        application, AppDatabase::class.java, "Entertainment.db")
        .allowMainThreadQueries().build()
}

fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
    return appDatabase.movieDao()
}
