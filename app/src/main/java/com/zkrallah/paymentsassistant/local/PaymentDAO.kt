package com.zkrallah.paymentsassistant.local

import androidx.room.*
import com.zkrallah.paymentsassistant.local.entities.Payment
import com.zkrallah.paymentsassistant.local.entities.User

@Dao
interface PaymentDAO {

    @Query("select * from payments_table")
    fun getPayments(): List<Payment>

    @Insert
    suspend fun insertPayment(payment: Payment?)

    @Query("DELETE from payments_table WHERE paymentId = :paymentId")
    suspend fun deletePayment(paymentId: Long): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePayment(user: User)
}