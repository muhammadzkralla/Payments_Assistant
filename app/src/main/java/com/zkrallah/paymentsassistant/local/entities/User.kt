package com.zkrallah.paymentsassistant.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    var name: String,
    var subscriptionDate: String,
    var endSubscriptionDate: String,
    var subscriptionPrice: String
){
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L
}
