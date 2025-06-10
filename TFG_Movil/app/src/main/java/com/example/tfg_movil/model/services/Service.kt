package com.example.tfg_movil.model.services

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Nombre")
    val nombre: String,

    @SerializedName("Abreviatura")
    val abreviatura: String,

    @SerializedName("Color")
    val color: String
) {
    constructor() : this(0, "", "", "")
}