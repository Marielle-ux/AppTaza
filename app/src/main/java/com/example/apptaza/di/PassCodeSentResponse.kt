package com.example.apptaza.di


import com.google.gson.annotations.SerializedName

data class PassCodeSentResponse(
    @SerializedName("message")
    val message: String // sent
)