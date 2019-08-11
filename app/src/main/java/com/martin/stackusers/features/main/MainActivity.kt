package com.martin.stackusers.features.main

import android.os.Bundle
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
import com.martin.stackusers.injection.ViewModelFactory
import com.martin.stackusers.models.User
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

    }

    private fun setupInfinityScrolling(layoutManager: LinearLayoutManager) {
        scrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadUsers(true)
            }
        }

        rvUsers.addOnScrollListener(scrollListener!!)
    }

    private fun setupSwipeToRefreshView() {
        vRefresh.setOnRefreshListener {
            viewModel.loadUsers()
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
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadUsers()
    }
}
