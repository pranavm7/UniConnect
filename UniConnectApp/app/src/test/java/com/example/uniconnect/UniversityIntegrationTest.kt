package com.example.uniconnect

import android.content.ContentValues.TAG
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.uniconnect.dto.University
import com.example.uniconnect.service.UniversityService
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UniversityIntegrationTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var universityService: UniversityService
    private var allUniversities: List<University>? = ArrayList()

    @Test
    fun `Given service connects to University JSON stream when data are read and parsed then university collection should be greater than zero`() =
        runTest {
            givenUniversityServiceIsInitialized()
            whenServiceDataAreReadAndParsed()
            thenTheUniversityCollectionSizeShouldBeGreaterThanZero()
        }

    @Test
    fun `Given service connects to University JSON stream, When data are read and parsed, Then the university collection should contain Marywood University`() =
        runTest {
            givenUniversityServiceIsInitialized()
            whenServiceDataAreReadAndParsed()
            thenTheUniversityCollectionShouldContainMarywoodUniversity()
        }

    private fun givenUniversityServiceIsInitialized() {
        universityService = UniversityService()
    }

    private suspend fun whenServiceDataAreReadAndParsed() {
        allUniversities = universityService.fetchUniversities()
    }

    private fun thenTheUniversityCollectionSizeShouldBeGreaterThanZero() {
        Assert.assertNotNull(allUniversities)
        allUniversities?.let {
            Assert.assertTrue(it.isNotEmpty())
        } ?: Log.e(TAG, "allUniversities was null")
    }

    private fun thenTheUniversityCollectionShouldContainMarywoodUniversity() {
        Assert.assertNotNull(allUniversities)
        allUniversities?.let {
            Assert.assertTrue(it.isNotEmpty())
            var containsMarywoodUniversity = false
            for (university in it) {
                if (university.name == "Marywood University" && university.code == "US") {
                    containsMarywoodUniversity = true
                }
            }
            Assert.assertTrue(containsMarywoodUniversity)
        } ?: Log.e(TAG, "allUniversities was null")
    }
}

