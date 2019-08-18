package com.martin.stackusers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.martin.stackusers.features.userdetails.UserDetailsViewModel
import com.martin.stackusers.models.Badge
import com.martin.stackusers.models.User
import com.martin.stackusers.repositories.RepositoryCallback
import com.martin.stackusers.repositories.UserRepository
import com.nhaarman.mockitokotlin2.times
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class UserDetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var userDetailsViewModel: UserDetailsViewModel

    @Mock
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.userDetailsViewModel = UserDetailsViewModel(userRepository)
    }

    @Test
    fun loadData_userExist() {
        val user = User().apply {
            userId = 1
            profileImage = "test.png"
            reputation = 1000
            badgeCounts = Badge().apply {
                gold = 2
                silver = 3
                bronze = 4
            }
            location = "Ho Chi Minh City"
            displayName = "Martin"
        }

        doAnswer { invocation ->
            val callback = invocation.getArgument<RepositoryCallback<User>>(1)
            callback.onSuccess(user)
            null
        }
            .`when`(userRepository).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        userDetailsViewModel.loadData(user.userId)

        verify(userRepository, times(1)).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assertEquals(user.profileImage, userDetailsViewModel.profileImage.value)
        assertEquals(user.reputation.toString(), userDetailsViewModel.reputation.get())
        assertEquals(user.badgeCounts.gold.toString(), userDetailsViewModel.gold.get())
        assertEquals(user.badgeCounts.silver.toString(), userDetailsViewModel.silver.get())
        assertEquals(user.badgeCounts.bronze.toString(), userDetailsViewModel.bronze.get())
        assertEquals(user.location, userDetailsViewModel.location.get())
        assertEquals(user.displayName, userDetailsViewModel.displayName.get())
    }

    @Test
    fun loadData_userNotExist() {
        doAnswer { invocation ->
            val callback = invocation.getArgument<RepositoryCallback<User>>(1)
            callback.onSuccess(null)
            null
        }
            .`when`(userRepository).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        val userId = 1L
        userDetailsViewModel.loadData(userId)

        verify(userRepository, times(1)).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assertEquals(UserDetailsViewModel.NOTIFICATION_USER_NOT_FOUND, userDetailsViewModel.notification.value)
    }

    @Test
    fun loadData_failure() {
        doAnswer { invocation ->
            val callback = invocation.getArgument<RepositoryCallback<User>>(1)
            callback.onFailure()
            null
        }
            .`when`(userRepository).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        val userId = 1L
        userDetailsViewModel.loadData(userId)

        verify(userRepository, times(1)).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assertEquals(UserDetailsViewModel.NOTIFICATION_USER_NOT_FOUND, userDetailsViewModel.notification.value)
    }
}