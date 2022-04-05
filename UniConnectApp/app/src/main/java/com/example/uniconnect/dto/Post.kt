package com.example.uniconnect.dto

data class Post(var postId : String = "", var title: String = "", var description: String = "") {
    override fun toString(): String {
        return "$title - $description"
    }
}