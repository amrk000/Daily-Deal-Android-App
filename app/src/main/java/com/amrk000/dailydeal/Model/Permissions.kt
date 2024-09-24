package com.amrk000.dailydeal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "permissions")
data class Permissions(
    @PrimaryKey(autoGenerate = false) val role: String,
    val orderItem: Boolean,
    val adminDashboard: Boolean,
    val addItem: Boolean,
    val editItem: Boolean,
    val deleteItem: Boolean
)
