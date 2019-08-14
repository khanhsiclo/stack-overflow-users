package com.martin.stackusers.features.userdetails

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.martin.stackusers.R
import com.martin.stackusers.application.MainApplication
import com.martin.stackusers.databinding.ActivityUserDetailsBinding
import com.martin.stackusers.injection.ViewModelFactory
import com.martin.stackusers.utils.Constants
import kotlinx.android.synthetic.main.activity_user_details.*
import javax.inject.Inject

class UserDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: ActivityUserDetailsBinding
    lateinit var viewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        setupBinding()
        initView()
        observeChanges()
    }

    private fun setupBinding() {
        (application as MainApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)
        viewModel = ViewModelProvider(this, viewModelFactory)[UserDetailsViewModel::class.java]
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }

    private fun initView() {
        setTitle(R.string.user_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeChanges() {
        viewModel.notification.observe(this, Observer {
            it?.let { notification ->
                when (notification) {
                    UserDetailsViewModel.NOTIFICATION_USER_NOT_FOUND -> showUserNotFoundMessage()
                }

                viewModel.notification.value = null
            }
        })

        viewModel.profileImage.observe(this, Observer {
            it?.let {
                Glide.with(this@UserDetailsActivity)
                    .load(it)
                    .into(ivAvatar)
            }
        })
    }

    private fun showUserNotFoundMessage() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        val userId = intent.getLongExtra(Constants.INTENT_USER_ID, 0)
        viewModel.loadData(userId)
    }
}
