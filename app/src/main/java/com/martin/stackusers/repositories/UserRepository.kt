package com.martin.stackusers.repositories

import androidx.lifecycle.LiveData
import com.martin.stackusers.models.User

interface UserRepository {
    var users: LiveData<List<User>>

    fun getUsers(page: Int, callback: RepositoryCallback<Boolean>? = null)
    fun getUser(userId: Long, callback: RepositoryCallback<User>)
}