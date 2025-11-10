package com.example.apptaza.network

import com.example.apptaza.di.ConfirmOtpRequest
import com.example.apptaza.di.SendOtpRequest
import com.example.apptaza.signUp.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SignUpRepositoryImpl(
    private val api: ApiService
) : SignUpRepository {

    override suspend fun sendOtp(phone: String) = withContext(Dispatchers.IO) {
        try {
            val body = api.sendOtp(SendOtpRequest(phone))
            if (!body.message.equals("sent", ignoreCase = true)) {
                throw IOException(body.message.ifBlank { "Не удалось отправить код" })
            }
        } catch (e: HttpException) {
            throw IOException(
                "HTTP ${e.code()}: ${e.response()?.errorBody()?.string().orEmpty()}",
                e
            )
        }
    }

    override suspend fun confirmOtp(phone: String, code: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val body = api.confirmOtp(ConfirmOtpRequest(phone, code))
                body.message.isNotBlank()
            } catch (e: HttpException) {
                throw IOException(
                    "HTTP ${e.code()}: ${
                        e.response()?.errorBody()?.string().orEmpty()
                    }", e
                )
            }
        }
}