package com.martin.stackusers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.martin.stackusers.database.converters.Converters
import com.martin.stackusers.database.daos.UserDao
import com.martin.stackusers.models.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}