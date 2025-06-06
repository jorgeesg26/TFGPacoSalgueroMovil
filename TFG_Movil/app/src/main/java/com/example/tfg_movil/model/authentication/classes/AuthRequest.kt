package com.example.tfg_movil.model.authentication.classes

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("UserEmailOrNickname") val userEmailOrNickname: String,
    @SerializedName("UserPassword") val password: String
)