package com.zkrallah.paymentsassistant.local

import androidx.room.*
import com.zkrallah.paymentsassistant.local.entities.User
import com.zkrallah.paymentsassistant.local.entities.UserWithPayments

@Dao
interface UserDAO {

    @Query("select * from users_table")
    fun getUsers(): List<User>

    @Insert
    suspend fun insertUser(user: User?)

    @Query("DELETE from users_table WHERE userId = :userId")
    suspend fun deleteUser(userId: Long): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Transaction
    @Query("SELECT * FROM users_table WHERE userId=:userId")
    fun getUserWithPaymentsById(userId: Long): UserWithPayments?
}