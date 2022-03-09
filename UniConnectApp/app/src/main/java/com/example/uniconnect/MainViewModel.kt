package com.example.uniconnect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uniconnect.dto.University
import com.example.uniconnect.service.UniversityService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var universities: MutableLiveData<List<University>> = MutableLiveData<List<University>>()
    var UniversityService: UniversityService = UniversityService()

    fun fetchUniversities() {
        viewModelScope.launch {
            var innerUniversity = UniversityService.fetchUniversities()
            universities.postValue(innerUniversity)
        }
    }
}
