package com.example.uniconnect

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.uniconnect.dto.Post
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MainViewModel(var universityService: IUniversityService = UniversityService()) : ViewModel() {
    private lateinit var firestore : FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    fun savePost (post: Post) {
        val doc = if (post.postId == null || post.postId.isEmpty()) {
            // insert
            firestore.collection("posts").document()
        } else {
            // update
            firestore.collection("posts").document(post.postId)
        }
        post.postId = doc.id
        val handle = doc.set(post)
        handle.addOnSuccessListener { Log.d("Firebase", "document saved") }
        handle.addOnFailureListener { Log.d("Firebase", "Error saving document $it")}
    }
}