package com.martin.stackusers.repositories

import androidx.lifecycle.LiveData
import com.martin.stackusers.models.User

interface UserRepository {
    fun getAllUsers(): LiveData<List<User>>
    fun getUsersByPage(page: Int, callback: RepositoryCallback<Boolean>? = null)
    fun getUser(userId: Long, callback: RepositoryCallback<User>)
}