package com.martin.stackusers.features.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martin.stackusers.R
import com.martin.stackusers.application.MainApplication
import com.martin.stackusers.customviews.EndlessScrollListener
import com.martin.stackusers.databinding.ActivityMainBinding
import com.martin.stackusers.features.userdetails.UserDetailsActivity
import com.martin.stackusers.injection.ViewModelFactory
import com.martin.stackusers.models.User
import com.martin.stackusers.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private var adapter: UserAdapter? = null
    private var scrollListener: EndlessScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        initView()
        observeChanges()
    }

    private fun setupBinding() {
        (application as MainApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private fun initView() {
        setTitle(R.string.users)
        vRefresh.isRefreshing = true
        setupUsersList()
    }

    private fun setupUsersList() {
        adapter = UserAdapter()
        adapter?.listener = object : UserAdapter.Listener {
            override fun onUserSelected(user: User) {
                openUserDetails(user)
            }
        }
        val layoutManager = LinearLayoutManager(this)
        rvUsers.layoutManager = layoutManager
        rvUsers.adapter = adapter

        setupInfinityScrolling(layoutManager)
        setupSwipeToRefreshView()
    }

    private fun openUserDetails(user: User) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra(Constants.INTENT_USER_ID, user.userId)
        startActivity(intent)
    }

    private fun setupInfinityScrolling(layoutManager: LinearLayoutManager) {
        scrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadUsers(this@MainActivity, true)
            }
        }

        rvUsers.addOnScrollListener(scrollListener!!)
    }

    private fun setupSwipeToRefreshView() {
        vRefresh.setOnRefreshListener {
            viewModel.loadUsers(this@MainActivity)
        }
    }

    private fun observeChanges() {
        viewModel.users.observe(this, Observer {
            it?.let { users ->
                adapter?.swapData(users, viewModel.hasMore)
                if (viewModel.currentPage == 1) scrollListener?.resetState()
            }
        })

        viewModel.loadingCompleted.observe(this, Observer { success ->
            success?.let {
                vRefresh.isRefreshing = false
                viewModel.loadingCompleted.value = null
            }
        })

        viewModel.notification.observe(this, Observer {
            it?.let { notification ->
                when (notification) {
                    MainViewModel.NOTIFICATION_NETWORK_UNAVAILABLE -> {
                        showToastMessage(R.string.message_network_unavailable)
                        scrollListener?.resetState()
                    }
                    MainViewModel.NOTIFICATION_LOAD_FAILED_UNEXPECTED -> {
                        showToastMessage(R.string.message_load_user_failed)
                        scrollListener?.resetState()
                    }
                }

                viewModel.notification.value = null
            }
        })
    }

    private fun showToastMessage(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadUsers(this)
    }
}
