package com.example.uniconnect.dao

import com.example.uniconnect.dto.University
import retrofit2.Call
import retrofit2.http.GET

interface IUniversityDAO {
    /**
     * This method returns the list of university implementing retrofit,
     *
     * @return List of universities
     */
    @GET("/search")
    fun getAllUniversities() : Call<List<University>>
}