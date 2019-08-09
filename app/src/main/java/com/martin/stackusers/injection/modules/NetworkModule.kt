package com.martin.stackusers.injection.modules

import com.martin.stackusers.networking.RetrofitClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofitClient() = RetrofitClient()
}