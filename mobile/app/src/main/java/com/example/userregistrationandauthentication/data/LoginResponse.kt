package com.example.userregistrationandauthentication.data

data class LoginResponse(
    val token: String,
    val user: UserResponse
)