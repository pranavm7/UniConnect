package com.example.uniconnect.dao

import com.example.uniconnect.dto.University

interface IUniversityService {
    /**
     * A method to return a list of universities. Function runs in a separate thread.
     *
     * @return Returns a list of universities
     */
    //Saw this in the service folder. So I moved the file here. Will this be used in the future?
    //If not just remove it as it seems like IUniversityDAO got you covered
    suspend fun fetchUniversities(): List<University>?
}