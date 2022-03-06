package com.example.uniconnect.service

import com.example.uniconnect.dto.University

interface IUniversityService {
    /**
     * A method to return a list of universities. Function runs in a separate thread.
     *
     * @return Returns a list of universities
     */
    suspend fun fetchUniversities(): List<University>?
}