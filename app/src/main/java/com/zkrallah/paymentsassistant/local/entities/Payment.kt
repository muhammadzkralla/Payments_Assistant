package com.zkrallah.paymentsassistant.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments_table")
data class Payment(
    var paymentDate: String,
    var price: String,
    var userId: Long,
    var userName: String
){
    @PrimaryKey(autoGenerate = true)
    var paymentId: Long = 0L
}
