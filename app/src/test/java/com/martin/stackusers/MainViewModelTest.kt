package com.martin.stackusers

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.martin.stackusers.features.main.MainViewModel
import com.martin.stackusers.models.Badge
import com.martin.stackusers.models.User
import com.martin.stackusers.repositories.RepositoryCallback
import com.martin.stackusers.repositories.UserRepository
import com.martin.stackusers.utils.NetworkUtils
import com.nhaarman.mockitokotlin2.times
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var context: Context
    private var mockUsers = MutableLiveData<List<User>>()

    @Mock
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(userRepository.getAllUsers()).thenReturn(mockUsers)
        this.mainViewModel = MainViewModel(userRepository)
        context = InstrumentationRegistry.getInstrumentation().context
    }

    private fun getMockUsers(): List<User> {
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
        return listOf(user)
    }

    private fun getAnotherMockUsers(): List<User> {
        val user = User().apply {
            userId = 2
            profileImage = "image.png"
            reputation = 1234
            badgeCounts = Badge().apply {
                gold = 5
                silver = 6
                bronze = 7
            }
            location = "Ha Noi City"
            displayName = "Maxime"
        }
        return listOf(user)
    }

    private fun mockNetworkAvailability(available: Boolean) {
        mockkObject(NetworkUtils)
        every { NetworkUtils.isInternetAvailable(context) } returns available
    }

    @Test
    fun loadData_getUsersFirstPageSuccess() {
        mockNetworkAvailability(true)

        val hasMore = true
        val users = getMockUsers()
        doAnswer { invocation ->
            mockUsers.value = users
            val callback = invocation.getArgument<RepositoryCallback<Boolean>>(1)
            callback.onSuccess(hasMore)
            null
        }
            .`when`(userRepository).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        val currentPage = 0
        mainViewModel.currentPage = currentPage
        mainViewModel.loadUsers(context, false)

        verify(userRepository, times(1)).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assert(currentPage+1 == mainViewModel.currentPage)
        assert(hasMore == mainViewModel.hasMore)
        assert(users == mainViewModel.users.value)
        assert(true == mainViewModel.loadingCompleted.value)
    }

    @Test
    fun loadData_getUsersFailure() {
        mockNetworkAvailability(true)

        doAnswer { invocation ->
            val callback = invocation.getArgument<RepositoryCallback<Boolean>>(1)
            callback.onFailure()
            null
        }
            .`when`(userRepository).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        val currentPage = 0
        mainViewModel.currentPage = currentPage
        mainViewModel.loadUsers(context, false)

        verify(userRepository, times(1)).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assert(currentPage == mainViewModel.currentPage)
        assert(mainViewModel.users.value.isNullOrEmpty())
        assert(MainViewModel.NOTIFICATION_LOAD_FAILED_UNEXPECTED == mainViewModel.notification.value)
    }

    @Test
    fun loadData_getMoreUsersSuccess() {
        mockNetworkAvailability(true)

        val hasMore = true
        mockUsers.value = getMockUsers()
        val users = getAnotherMockUsers()
        doAnswer { invocation ->
            val temp = ArrayList<User>()
            temp.addAll(mockUsers.value!!)
            temp.addAll(users)
            mockUsers.value = temp
            val callback = invocation.getArgument<RepositoryCallback<Boolean>>(1)
            callback.onSuccess(hasMore)
            null
        }
            .`when`(userRepository).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())

        val currentPage = 1
        mainViewModel.currentPage = currentPage
        mainViewModel.loadUsers(context, true)

        verify(userRepository, times(1)).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assert(currentPage+1 == mainViewModel.currentPage)
        assert(hasMore == mainViewModel.hasMore)
        assert(mockUsers.value == mainViewModel.users.value)
        assert(true == mainViewModel.loadingCompleted.value)
    }

    @Test
    fun loadData_getUsersNotCallNoConnection() {
        mockNetworkAvailability(false)

        val currentPage = 0
        val hasMore = true
        mainViewModel.currentPage = currentPage
        mainViewModel.hasMore = hasMore
        mainViewModel.loadUsers(context, true)

        verify(userRepository, never()).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assert(currentPage == mainViewModel.currentPage)
        assert(hasMore == mainViewModel.hasMore)
        assert(true == mainViewModel.loadingCompleted.value)
    }

    @Test
    fun loadData_getUsersNotCallReachEnd() {
        mockNetworkAvailability(true)

        val currentPage = 0
        val hasMore = false
        mainViewModel.currentPage = currentPage
        mainViewModel.hasMore = hasMore
        mainViewModel.loadUsers(context, true)

        verify(userRepository, never()).getUsersByPage(com.nhaarman.mockitokotlin2.any(), com.nhaarman.mockitokotlin2.any())
        assert(currentPage == mainViewModel.currentPage)
        assert(hasMore == mainViewModel.hasMore)
        assert(true == mainViewModel.loadingCompleted.value)
    }
}