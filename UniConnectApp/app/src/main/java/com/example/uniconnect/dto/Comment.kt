package com.example.uniconnect.dto

data class Comment(
        var postId : String = "",
        var userId : String = "",
        var comment : String = "",
        var commentId : String? = "",
        var parentComment: String? = "") {
    override fun toString(): String {
        return "$userId - $comment"
    }
}