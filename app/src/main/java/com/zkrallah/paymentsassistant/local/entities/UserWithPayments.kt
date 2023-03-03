package com.zkrallah.paymentsassistant.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPayments(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val payments: List<Payment>
)
