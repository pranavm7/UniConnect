package com.example.uniconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.uniconnect.dto.University
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UniConnectUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mvm : MainViewModel

    private val mainThreadSurrogate = newSingleThreadContext("Main thread")

    @MockK
    lateinit var mockUniService : UniversityService

    @Before
    fun populateUniversities() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `given a view model with live data when populated with universities then results show UC` () {
        givenViewModelIsInitializedWithMockData()
        whenUniversityServiceFetchUniversitiesInvoked()
        thenResultsShouldContainUC()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val universities = ArrayList<University>()
        universities.add(University("Marywood University", "United States", "US"))
        universities.add(University("University of Cincinnati", "United States", "US"))
        universities.add(University("University of Petroleum and Energy Studies", "India", "IN"))

        coEvery { mockUniService.fetchUniversities() } returns universities

        mvm = MainViewModel(universityService = mockUniService)
    }

    private fun whenUniversityServiceFetchUniversitiesInvoked() {
        mvm.fetchUniversities()
    }

    private fun thenResultsShouldContainUC() {
        var allUniversities : List<University>? = ArrayList()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<University>> {
            override fun onChanged(receivedUniversities: List<University>?) {
                allUniversities = receivedUniversities
                latch.countDown()
                mvm.universities.removeObserver(this)
            }
        }
        mvm.universities.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allUniversities)
        assertTrue(allUniversities!!.isNotEmpty())
        var containsUniversityCincinnati = false
        allUniversities!!.forEach {
            if (it.name == "University of Cincinnati" && it.country.equals("United States")) {
                containsUniversityCincinnati = true
            }
        }
        assertTrue(containsUniversityCincinnati)
    }
}
