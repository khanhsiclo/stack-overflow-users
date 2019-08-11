package com.martin.stackusers.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.martin.stackusers.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Insert(onConflict = REPLACE)
    fun insertUsers(vararg users: User)

    @Query("DELETE FROM user")
    fun deleteAll()
}