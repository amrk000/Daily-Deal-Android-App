package com.amrk000.dailydeal.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserData::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ItemData::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("itemId"),
            onDelete = ForeignKey.NO_ACTION
        )
])
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val itemId: Long,
    val date: String //UTC DATE
)
