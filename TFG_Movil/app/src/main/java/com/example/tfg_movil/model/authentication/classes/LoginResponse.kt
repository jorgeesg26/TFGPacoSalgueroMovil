package com.example.tfg_movil.model.authentication.classes

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("StringToken") val accessToken: String,
    @SerializedName("UserId") val userId: Int
)