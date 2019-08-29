package com.example.nghiamvvm

import android.app.Activity
import android.app.Application
import com.example.nghiamvvm.di_dagger.module.DaggerAppComponent
import com.example.nghiamvvm.di_koin.Api2Module
import com.example.nghiamvvm.di_koin.Db2Module
import com.example.nghiamvvm.di_koin.Test2Module
import com.example.nghiamvvm.di_koin.ViewModel2Module
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import javax.inject.Inject

class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()

        // Init Dagger
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        // Init Koin
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(
                Api2Module,
                Db2Module,
                Test2Module,
                ViewModel2Module
            ))
        }
    }
}