package com.martin.stackusers.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.martin.stackusers.models.User

interface UserRepository {
    var users: LiveData<List<User>>

    fun getUsers(page: Int, callback: RepositoryCallback<Boolean>? = null)
}