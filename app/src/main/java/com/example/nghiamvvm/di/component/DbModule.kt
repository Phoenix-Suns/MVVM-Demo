package com.example.nghiamvvm.di.component

import android.app.Application
import androidx.room.Room
import com.example.nghiamvvm.data.AppDatabase
import com.example.nghiamvvm.data.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, "Entertainment.db")
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    internal fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }
}