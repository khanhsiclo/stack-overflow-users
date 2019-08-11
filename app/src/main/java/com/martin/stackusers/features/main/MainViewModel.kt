package com.martin.stackusers.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martin.stackusers.models.User
import com.martin.stackusers.repositories.RepositoryCallback
import com.martin.stackusers.repositories.UserRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    var users: LiveData<List<User>> = userRepository.users
    var currentPage: Int = 0
    var hasMore: Boolean = true
    var loadingCompleted = MutableLiveData<Boolean>()

    fun loadUsers(loadMore: Boolean = false) {
        currentPage = if (loadMore) currentPage+1 else 1
        if (currentPage == 1) hasMore = true

        userRepository.getUsers(currentPage, object : RepositoryCallback {
            override fun onSuccess() {
                loadingCompleted.value = true
            }

            override fun onFailure() {
                loadingCompleted.value = true
            }
        })
    }
}