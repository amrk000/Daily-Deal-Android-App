package com.amrk000.dailydeal.util

import java.util.regex.Pattern

object TextFieldValidator{
    //Patterns
    private val nameRegex: Pattern
    private val emailRegex: Pattern
    private val passwordRegex: Pattern
    private val phoneNumberRegex: Pattern

    init {
        nameRegex = Pattern.compile("((\\w|\\s|\\.|-)+){2,32}")
        emailRegex = Pattern.compile("(\\.*\\w+)+@([a-z]+|[A-Z]+)(\\.com|\\.net|\\.org|\\.co|\\.sa)(\\.([a-z]+|[A-Z]+)+)?")
        passwordRegex = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*]).\\S{8,}")
        phoneNumberRegex = Pattern.compile("\\+?([0-9]){10,14}")
    }


    fun isValidName(username: String): Boolean {
        return nameRegex.matcher(username).matches()
    }

    fun isValidEmail(email: String): Boolean {
        return emailRegex.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return passwordRegex.matcher(password).matches()
    }

    fun isValidPhone(phoneNumber: String): Boolean {
        return phoneNumberRegex.matcher(phoneNumber).matches()
    }

}