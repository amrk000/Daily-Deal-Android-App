package com.amrk000.dailydeal.Model

data class UserOrderData(
    val id: Long = 0,
    var name: String,
    var image: String, //Base 64
    var restaurant: String,
    var day: String,
    val date: String //UTC DATE
)
