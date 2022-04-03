package com.example.uniconnect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.University
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch

class MainViewModel(var universityService: IUniversityService = UniversityService()) : ViewModel() {
    val universities: MutableLiveData<List<University>> = MutableLiveData<List<University>>()
    val posts: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToPost()
    }
    fun fetchUniversities() {
        viewModelScope.launch {
            var innerUniversities = universityService.fetchUniversities()
            universities.postValue(innerUniversities)
        }
    }

    fun listenToPost() {
        firestore.collection("posts").addSnapshotListener {
                snapshot, error ->
            // see of we received an error
            if (error != null) {
                Log.w("listen failed.", error)
                return@addSnapshotListener
            }
            // if we reached this point, there was not an error, and we have data.
            snapshot?.let {
                val allPosts = ArrayList<Post>()

                /*allPosts.add(Post(NEW_SPECIMEN))*/

                val documents = snapshot.documents
                documents.forEach {
                    val post = it.toObject(Post::class.java)
                    post?.let {
                        allPosts.add(post)
                    }
                }
                // we have a populated collection of posts.
                posts.value = allPosts
            }
        }
    }

    fun savePost (post: Post) {
        val doc = if (post.postId == "" || post.postId.isEmpty()) {
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

    fun deletePost (post: Post) {
        var postCollection = firestore.collection("posts").document(post.postId)
        postCollection.delete()
    }

}