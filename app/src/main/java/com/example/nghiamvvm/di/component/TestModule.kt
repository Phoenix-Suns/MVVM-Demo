package com.example.nghiamvvm.di.component

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestModule {
    @Provides
    @Singleton
    internal fun providerAgeToString(nghia: Int): String {
        return nghia.toString()
    }

    @Provides
    @Singleton
    internal fun providerAge(): Int {
        return 123456
    }

    // Tự Tìm hàm trả về Int, để thêm tham số vào ProviderName
    // chỉ duy nhất 1 hàm trả về Int
}