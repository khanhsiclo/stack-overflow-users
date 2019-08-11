package com.martin.stackusers.injection

import com.martin.stackusers.features.main.MainActivity
import com.martin.stackusers.injection.modules.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RepositoryModule::class,
    ViewModelModule::class, NetworkModule::class, DatabaseModule::class])
@Singleton
interface AppComponent {
    fun inject(target: MainActivity)
}