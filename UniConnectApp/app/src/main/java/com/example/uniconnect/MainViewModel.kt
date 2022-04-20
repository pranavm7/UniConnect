package com.example.uniconnect

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniconnect.dto.Comment
import com.example.uniconnect.dto.Photo
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.University
import com.example.uniconnect.dto.User
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class MainViewModel(var universityService: IUniversityService = UniversityService()) : ViewModel() {
    val photos: ArrayList<Photo> = ArrayList<Photo>()
    var user: User? = null
    val universities: MutableLiveData<List<University>> = MutableLiveData<List<University>>()
    val postsOfCurrentUser: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance().getReference()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToThisUserPost()
    }
    fun fetchUniversities() {
        viewModelScope.launch {
            val innerUniversities = universityService.fetchUniversities()
            universities.postValue(innerUniversities)
        }
    }

    fun listenToThisUserPost() {
        user?.let { user ->
            firestore.collection("users").document(user.uid).collection("posts").addSnapshotListener { snapshot, error ->
                // see if we received an error
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
                    postsOfCurrentUser.value = allPosts
                }
            }
        }
    }

    fun saveUser() {
        user?.let {
                user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "User Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "User save failed $user") }

        }
    }

    fun savePost (post: Post) {
        user?.let { user ->
            val doc = if (post.postId == "" || post.postId.isEmpty()) {
                // insert
                firestore.collection("users").document(user.uid).collection("posts").document()
            } else {
                // update
                firestore.collection("users").document(user.uid).collection("posts").document(post.postId)
            }
            post.postId = doc.id
            val handle = doc.set(post)
            handle.addOnSuccessListener {
                Log.d("Firebase", "document saved")
                if(photos.isNotEmpty()){
                    uploadPhotos(post)
                }
            }
            handle.addOnFailureListener { Log.d("Firebase", "Error saving document $it") }
        }
    }


    private fun uploadPhotos(post: Post) {
        photos.forEach{
            photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo, post)

                }
            }
            uploadTask.addOnFailureListener{
                Log.e(TAG, it.message ?: "No message")
            }
        }
    }

    private fun updatePhotoDatabase(photo: Photo, post: Post) {
        user?.let {
            user->
            var photoCollection = firestore.collection("users").document(user.uid).collection("posts").document(post.postId).collection("photos")
            var handle = photoCollection.add(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully updated photo metadata")
                photo.id = it.id
                firestore.collection("users").document(user.uid).collection("posts").document(post.postId).collection("photos").document(photo.id).set(photo)
            }
            handle.addOnFailureListener{
                Log.e(TAG, "Error updating photo data: ${it.message}")
            }
        }
    }

    fun saveUser() {
        user?.let {
                user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "User Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "User save failed $user") }

        }
    }
    fun deletePost (post: Post) {
        user?.let { user ->
            val doc = firestore.collection("users").document(user.uid).collection("posts").document(post.postId)
            doc.delete()
                .addOnSuccessListener { Log.d("Firebase", "Post Deleted") }
                .addOnFailureListener { Log.d("Firebase", "Error Deleting Post ${it.message}") }
        }
    }

    fun saveComment(comment: Comment) {
        user?.let { user ->
            val handle = if(comment.commentId == "" || comment.commentId.isNullOrBlank()){
                // insert
                firestore.collection("comments").add(comment)
            } else {
                //Update
                firestore.collection("comments").document("${comment.commentId}").set(comment)
            }
            handle.addOnSuccessListener { Log.d("Firebase", "comment saved!") }
            handle.addOnFailureListener { Log.d("Firebase",
                                    "Error saving comment ${comment.commentId} \n ${it.message}",
                                        handle.exception) }
        }
    }

    fun deleteComment(comment: Comment) {
        user?.let {
            val owner = comment.userId
            if (user!!.uid.equals(owner)) {
                firestore.collection("comments")
                    .document("${comment.commentId}")
                    .delete()
                    .addOnSuccessListener { Log.d("Firebase", "Comment successfully deleted!") }
                    .addOnFailureListener { e ->
                        Log.w("Firebase","Error deleting comment ${comment.commentId}",e)
                    }
            }
        }
    }
}