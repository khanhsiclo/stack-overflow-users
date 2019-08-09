package com.martin.stackusers.injection.modules

import com.martin.stackusers.repositories.UserRepository
import com.martin.stackusers.repositories.impl.UserRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl
}