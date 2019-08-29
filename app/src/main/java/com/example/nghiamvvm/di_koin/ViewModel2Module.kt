package com.example.nghiamvvm.di_koin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nghiamvvm.helper.ViewModelFactory
import com.example.nghiamvvm.ui.MovieList2ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModel2Module = module {
    viewModel { MovieList2ViewModel(get(), get()) }
}

/*
fun bindViewModel2Factory(): ViewModelProvider.Factory {
    return object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ViewModelProvider.NewInstanceFactory().create(modelClass)
        }
    }
}*/
