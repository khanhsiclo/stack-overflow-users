package com.martin.stackusers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.martin.stackusers.database.daos.UserDao
import com.martin.stackusers.models.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}