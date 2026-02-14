package com.example.userregistrationandauthentication.data

data class UserResponse(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
    val isActive: Boolean
)
