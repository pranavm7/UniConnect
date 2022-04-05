package com.example.uniconnect.dto

data class Comment(var commentId : String = "", var postId : String = "", var userId : String = "", var comment : String = "") {
    override fun toString(): String {
        return "$userId - $comment"
    }
}