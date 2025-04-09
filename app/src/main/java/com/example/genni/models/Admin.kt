package com.example.genni.models

// Admin Data Model
data class Admin(
    val adminID: Int,
    val username: String,
    val password: String
) {
    constructor() : this (
        adminID = 0,
        username = "",
        password = ""
    )
}
