package com.amrk000.dailydeal.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = Permissions::class,
            parentColumns = arrayOf("role"),
            childColumns = arrayOf("role"),
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["email"], unique = true)]
)
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val role: String = "user"
)
