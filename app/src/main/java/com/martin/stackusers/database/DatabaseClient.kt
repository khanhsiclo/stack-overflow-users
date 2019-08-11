package com.martin.stackusers.database

import android.content.Context
import androidx.room.Room
import javax.inject.Inject

class DatabaseClient @Inject constructor(context: Context) {
    val appDatabase: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "StackUsersDb").build()
}