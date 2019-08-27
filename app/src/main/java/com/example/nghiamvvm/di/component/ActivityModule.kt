package com.example.nghiamvvm.di.component

import com.example.nghiamvvm.ui.FetchListByDaggerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeFetchListByDaggerActivity(): FetchListByDaggerActivity
}