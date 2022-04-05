package com.example.uniconnect

import com.example.uniconnect.dto.University
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UniConnectUnitTest {
    @Test
    fun `Given a University DTO when name is MaryWood University and country is United States`() {
        val university = University("MaryWood University", "United States", "US")
        assertTrue(university.name == "MaryWood University")
        assertTrue(university.country == "United States")
    }
}