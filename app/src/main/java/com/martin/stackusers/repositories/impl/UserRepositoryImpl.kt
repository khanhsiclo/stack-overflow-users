package com.martin.stackusers.repositories.impl

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.martin.stackusers.database.DatabaseClient
import com.martin.stackusers.models.User
import com.martin.stackusers.networking.RetrofitClient
import com.martin.stackusers.networking.models.UsersResponse
import com.martin.stackusers.repositories.RepositoryCallback
import com.martin.stackusers.repositories.UserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val retrofitClient: RetrofitClient,
    private val databaseClient: DatabaseClient) : UserRepository {

    override var users: LiveData<List<User>> = databaseClient.appDatabase.userDao().getAllUsers()

    override fun getUsers(page: Int, callback: RepositoryCallback?) {
        val call = retrofitClient.getService().getUsers(1)
        call.enqueue(object : Callback<UsersResponse> {
            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                callback?.onFailure()
            }

            @SuppressLint("CheckResult")
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!.items
                    Observable.just(users)
                        .subscribeOn(Schedulers.io())
                        .map {
                            databaseClient.appDatabase.userDao().insertUsers(*it.toTypedArray())
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            callback?.onSuccess()
                        }

                } else {
                    callback?.onFailure()
                }
            }
        })
    }
}