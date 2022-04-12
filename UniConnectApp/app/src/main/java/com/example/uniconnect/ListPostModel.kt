package com.example.uniconnect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.PostUserVM
import com.example.uniconnect.dto.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class ListPostModel :ViewModel() {
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    val allPostsAllUsers : MutableLiveData<List<PostUserVM>> = MutableLiveData<List<PostUserVM>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToAllUsers()
    }

    private fun listenToAllUsers() {
        //var users = ArrayList<User>()
        firestore.collection("users").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("listen failed", error)
                return@addSnapshotListener
            }
            snapshot?.let {
                val documents = snapshot.documents
                val postUsers = ArrayList<PostUserVM>()
                documents.forEach {
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
                                val documents = snapshot.documents
                                documents.forEach {
                                    val post = it.toObject(Post::class.java)
                                    post?.let {
                                        var postUser = user.displayName?.let { it1 -> PostUserVM(uid = user.uid, displayName = it1, postId = post.postId, title = post.title, description = post.description ) }
                                        if (postUser != null) {
                                            var isAdded : Boolean = false
                                            postUsers.forEach {
                                                if (it.postId == postUser.postId) {
                                                    isAdded = true
                                                }
                                            }
                                            if (!isAdded) {
                                                postUsers.add(postUser)
                                            }
                                        }
                                    }
                                    var newPostUsers = ArrayList<PostUserVM>()
                                    postUsers.forEach {
                                        newPostUsers.add(it)
                                    }
                                    // we have a populated collection of posts with user data.
                                    allPostsAllUsers.value = newPostUsers
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}