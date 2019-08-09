package com.martin.stackusers.repositories.impl

import com.martin.stackusers.networking.RetrofitClient
import com.martin.stackusers.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val retrofitClient: RetrofitClient) : UserRepository {
}