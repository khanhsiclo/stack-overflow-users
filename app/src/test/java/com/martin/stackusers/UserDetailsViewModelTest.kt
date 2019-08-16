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
            answerCount = 5
            questionCount = 6
        }

        doAnswer { invocation ->
            val callback = invocation.getArgument<RepositoryCallback<User>>(1)
            callback.onSuccess(user)
            null
        }
            .`when`(userRepository).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        userDetailsViewModel.loadData(user.userId)

        verify(userRepository, times(1)).getUser(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assertEquals(userDetailsViewModel.profileImage.value, user.profileImage)
        assertEquals(userDetailsViewModel.reputation.get(), user.reputation.toString())
        assertEquals(userDetailsViewModel.gold.get(), user.badgeCounts.gold.toString())
        assertEquals(userDetailsViewModel.silver.get(), user.badgeCounts.silver.toString())
        assertEquals(userDetailsViewModel.bronze.get(), user.badgeCounts.bronze.toString())
        assertEquals(userDetailsViewModel.location.get(), user.location)
        assertEquals(userDetailsViewModel.displayName.get(), user.displayName)
        assertEquals(userDetailsViewModel.answers.get(), user.answerCount.toString())
        assertEquals(userDetailsViewModel.questions.get(), user.questionCount.toString())
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
        assertEquals(userDetailsViewModel.notification.value, UserDetailsViewModel.NOTIFICATION_USER_NOT_FOUND)
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
        assertEquals(userDetailsViewModel.notification.value, UserDetailsViewModel.NOTIFICATION_USER_NOT_FOUND)
    }
}