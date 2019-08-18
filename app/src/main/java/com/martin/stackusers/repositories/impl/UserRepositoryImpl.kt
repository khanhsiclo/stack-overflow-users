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

    override fun getAllUsers(): LiveData<List<User>> = databaseClient.appDatabase.userDao().getAllUsers()

    override fun getUsersByPage(page: Int, callback: RepositoryCallback<Boolean>?) {
        val call = retrofitClient.getService().getUsers(page)
        call.enqueue(object : Callback<UsersResponse> {
            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                callback?.onFailure()
            }

            @SuppressLint("CheckResult")
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val res = response.body()!!
                    Observable.just(res)
                        .subscribeOn(Schedulers.io())
                        .map {
                            if (page == 1) {
                                databaseClient.appDatabase.userDao().deleteAll()
                            }
                            databaseClient.appDatabase.userDao().insertUsers(*it.items.toTypedArray())
                            return@map res.hasMore
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { hasMore ->
                            callback?.onSuccess(hasMore)
                        }

                } else {
                    callback?.onFailure()
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    override fun getUser(userId: Long, callback: RepositoryCallback<User>) {
        Observable.just(userId)
            .subscribeOn(Schedulers.io())
            .map {
                return@map databaseClient.appDatabase.userDao().getUserById(userId)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                callback.onSuccess(it)
            }
    }
}