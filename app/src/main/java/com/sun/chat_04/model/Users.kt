package com.sun.chat_04.model

data class Users(
    val idUser: String?, val userName: String?, val Age: Int?,
    val gender: Int?, val bio: String?,
    val linkAvatar: String?, val linkBackground: String?,
    var isOnline: Int?
)