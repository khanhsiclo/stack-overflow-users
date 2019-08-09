package com.martin.stackusers.injection

import com.martin.stackusers.features.main.MainActivity
import com.martin.stackusers.injection.modules.AppModule
import com.martin.stackusers.injection.modules.NetworkModule
import com.martin.stackusers.injection.modules.RepositoryModule
import com.martin.stackusers.injection.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RepositoryModule::class,
    ViewModelModule::class, NetworkModule::class])
@Singleton
interface AppComponent {
    fun inject(target: MainActivity)
}