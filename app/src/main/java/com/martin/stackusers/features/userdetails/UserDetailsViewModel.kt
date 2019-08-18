package com.martin.stackusers.features.userdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martin.stackusers.models.User
import com.martin.stackusers.repositories.RepositoryCallback
import com.martin.stackusers.repositories.UserRepository
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    companion object {
        const val NOTIFICATION_USER_NOT_FOUND = 1
    }

    val notification = MutableLiveData<Int>()
    val profileImage = MutableLiveData<String>()
    val reputation = ObservableField("")
    val gold = ObservableField("")
    val silver = ObservableField("")
    val bronze = ObservableField("")
    val location = ObservableField("")
    val displayName = ObservableField("")

    fun loadData(userId: Long) {
        userRepository.getUser(userId, object : RepositoryCallback<User> {
            override fun onSuccess(result: User?) {
                if (result == null) {
                    notification.value = NOTIFICATION_USER_NOT_FOUND
                    return
                }

                bindData(result)
            }

            override fun onFailure(error: String?) {
                notification.value = NOTIFICATION_USER_NOT_FOUND
            }
        })
    }

    private fun bindData(user: User) {
        profileImage.value = user.profileImage
        reputation.set(user.reputation.toString())
        gold.set(user.badgeCounts.gold.toString())
        silver.set(user.badgeCounts.silver.toString())
        bronze.set(user.badgeCounts.bronze.toString())
        location.set(user.location)
        displayName.set(user.displayName)
    }
}