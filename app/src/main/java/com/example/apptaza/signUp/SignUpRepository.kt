package com.example.apptaza.signUp

interface SignUpRepository {
    suspend fun sendOtp(phone: String)
    suspend fun confirmOtp(phone: String, code: String): Boolean // вернём, например, token
}
