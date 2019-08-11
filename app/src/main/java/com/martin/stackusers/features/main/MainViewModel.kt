package com.martin.stackusers.features.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martin.stackusers.models.User
import com.martin.stackusers.repositories.RepositoryCallback
import com.martin.stackusers.repositories.UserRepository
import com.martin.stackusers.utils.NetworkUtils
import javax.inject.Inject

class MainViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    companion object {
        const val NOTIFICATION_NETWORK_UNAVAILABLE = 1
        const val NOTIFICATION_LOAD_FAILED_UNEXPECTED = 2
    }

    var users: LiveData<List<User>> = userRepository.users
    var currentPage: Int = 0
    var hasMore: Boolean = true
    val loadingCompleted = MutableLiveData<Boolean>()
    val notification = MutableLiveData<Int>()

    fun loadUsers(context: Context, loadMore: Boolean = false) {
        if (loadMore && !hasMore) return

        if (!NetworkUtils.isInternetAvailable(context)) {
            notification.value = NOTIFICATION_NETWORK_UNAVAILABLE
            loadingCompleted.value = true
            return
        }

        currentPage = if (loadMore) currentPage + 1 else 1
        if (currentPage == 1) hasMore = true

        userRepository.getUsers(currentPage, object : RepositoryCallback<Boolean> {
            override fun onSuccess(result: Boolean?) {
                loadingCompleted.value = true
                hasMore = result?: false
            }

            override fun onFailure(error: String?) {
                currentPage = if (currentPage > 1) currentPage-1 else 1
                loadingCompleted.value = true
                notification.value = NOTIFICATION_LOAD_FAILED_UNEXPECTED
            }
        })
    }
}