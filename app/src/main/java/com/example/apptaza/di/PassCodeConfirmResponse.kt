package com.example.apptaza.di


import com.google.gson.annotations.SerializedName

data class PassCodeConfirmResponse(
    @SerializedName("message")
    val message: String // confirmed
)