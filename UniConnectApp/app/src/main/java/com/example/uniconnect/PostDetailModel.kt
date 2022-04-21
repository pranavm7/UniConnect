package com.example.uniconnect

import android.util.Log
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.PostUserVM
import com.example.uniconnect.dto.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class PostDetailModel {

    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var postDetails: PostUserVM? = null

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun getPostDetails(postID: String, uID: String): PostUserVM? {
        firestore.collection("users").document(uID).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("listen failed", error)
                return@addSnapshotListener
            }
            snapshot?.let {
                val user = it.toObject(User::class.java)
                user?.let {
                    firestore.collection("users").document(user.uid).collection("posts").addSnapshotListener { snapshot, error ->
                        // see of we received an error
                        if (error != null) {
                            Log.w("listen failed.", error)
                            return@addSnapshotListener
                        }
                        // if we reached this point, there was not an error, and we have data.
                        snapshot?.let {
                            firestore.collection("users").document(uID).collection("posts").document(postID).addSnapshotListener { snapshot, error ->
                                // see of we received an error
                                if (error != null) {
                                    Log.w("listen failed.", error)
                                    return@addSnapshotListener
                                }
                                // if we reached this point, there was not an error, and we have data.
                                snapshot?.let {
                                    val post = it.toObject(Post::class.java)
                                    post?.let {
                                        var postUser = user.displayName?.let { it1 ->
                                            postDetails = PostUserVM(
                                                uid = user.uid,
                                                displayName = it1,
                                                postId = post.postId,
                                                title = post.title,
                                                description = post.description
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


    return postDetails}
}
