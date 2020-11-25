package edu.ktu.lab2defenseexample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Customer(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val firstName: String?,
    val surname: String?,
    val balance: Float,
)