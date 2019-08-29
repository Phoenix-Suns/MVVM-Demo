package com.example.nghiamvvm.di_koin

import org.koin.dsl.module

val Test2Module = module {
    single { providerAge() }
    single { providerAgeToString(get()) }
}

fun providerAge(): Int {
    return 4978533
}

fun providerAgeToString(nghia: Int): String {
    return nghia.toString()
}