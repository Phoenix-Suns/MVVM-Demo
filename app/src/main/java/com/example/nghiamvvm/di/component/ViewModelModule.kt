package com.example.testdagger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nghiamvvm.helper.ViewModelFactory
import com.example.nghiamvvm.helper.ViewModelKey
import com.example.nghiamvvm.ui.MovieListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel::class)
    protected abstract fun movieListViewModel(moviesListViewModel: MovieListViewModel): ViewModel
}