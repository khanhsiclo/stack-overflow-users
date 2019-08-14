package com.martin.stackusers.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.martin.stackusers.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY reputation DESC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE user_id=:id")
    fun getUserById(id: Long): User?

    @Insert(onConflict = REPLACE)
    fun insertUsers(vararg users: User)

    @Query("DELETE FROM user")
    fun deleteAll()
}