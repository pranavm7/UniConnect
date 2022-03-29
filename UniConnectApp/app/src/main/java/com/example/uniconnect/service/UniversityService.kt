package com.example.uniconnect.service

import com.example.uniconnect.RetrofitClientInstance
import com.example.uniconnect.dao.IUniversityDAO
import com.example.uniconnect.dao.IUniversityService
import com.example.uniconnect.dto.University
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class UniversityService : IUniversityService {
    override suspend fun fetchUniversities(): List<University>? {
        return withContext(Dispatchers.IO) {
            val retrofit = RetrofitClientInstance.retrofitInstance?.create(IUniversityDAO::class.java)
            val universities = async { retrofit?.getAllUniversities()}
            val result = universities.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}