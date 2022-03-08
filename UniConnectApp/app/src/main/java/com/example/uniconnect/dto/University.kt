package com.example.uniconnect.dto

import com.google.gson.annotations.SerializedName

data class University(
    @SerializedName("name") var name: String,
    @SerializedName("country") var country: String,
    @SerializedName("alpha_two_code") var code: String
) {
    override fun toString(): String {
        return "$name - $country - $code"
    }
}