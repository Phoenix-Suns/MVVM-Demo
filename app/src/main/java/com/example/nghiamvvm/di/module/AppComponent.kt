package com.example.nghiamvvm.di.module

import android.app.Application
import com.example.nghiamvvm.MyApplication
import com.example.nghiamvvm.di.component.ActivityModule
import com.example.nghiamvvm.di.component.ApiModule
import com.example.nghiamvvm.di.component.DbModule
import com.example.testdagger.di.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        ApiModule::class,
        DbModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    /*
     * This is our custom Application class
     * */
    fun inject(appController: MyApplication)
}