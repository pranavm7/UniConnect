package com.example.uniconnect

import androidx.lifecycle.ViewModel
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel(var universityService: IUniversityService = UniversityService()) : ViewModel() {
    private lateinit var firestore : FirebaseFirestore
}