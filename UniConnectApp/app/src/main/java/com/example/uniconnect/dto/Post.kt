package com.example.uniconnect.dto

data class Post(var postID: Int, var title: String, var description: String) {
    override fun toString(): String {
        return "$title - $description"
    }

}