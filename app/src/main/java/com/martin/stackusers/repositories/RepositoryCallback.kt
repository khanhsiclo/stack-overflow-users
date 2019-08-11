package com.martin.stackusers.repositories

interface RepositoryCallback<T> {
    fun onSuccess(result: T? = null)
    fun onFailure(error: String? = null)
}