package com.amrk000.dailydeal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var description: String,
    var image: String, //Base 64
    var restaurant: String,
    var day: String,
    var originalPrice: Double,
    var discountPrice: Double
)
