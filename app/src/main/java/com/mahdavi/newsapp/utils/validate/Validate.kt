package com.mahdavi.newsapp.utils.validate

interface Validate {
    fun validatePhoneNumber(phone: String): ValidateResult
    fun validateEmail(email: String): ValidateResult
    fun validatePassword(password: String): ValidateResult
}