package com.example.apptaza.network

import com.example.apptaza.di.ConfirmOtpRequest
import com.example.apptaza.di.PassCodeSentResponse
import com.example.apptaza.di.SendOtpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/auth/code/send")
    suspend fun sendOtp(@Body body: SendOtpRequest): PassCodeSentResponse

    @POST("/v1/auth/code/confirm")
    suspend fun confirmOtp(@Body body: ConfirmOtpRequest): PassCodeSentResponse
}
