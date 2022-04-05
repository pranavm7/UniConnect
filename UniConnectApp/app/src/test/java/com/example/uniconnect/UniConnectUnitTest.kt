package com.example.uniconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.University
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UniConnectUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mvm: MainViewModel

    private val mainThreadSurrogate = newSingleThreadContext("Main thread")

    @MockK
    lateinit var mockUniService: UniversityService


    lateinit var universityService: IUniversityService
    var allUniversities: List<University>? = ArrayList<University>()

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
    fun `Given a University DTO when name is MaryWood University and country is United States`() {
        val university = University("MaryWood University", "United States", "US")
        assertTrue(university.name.equals("MaryWood University"))
        assertTrue(university.country.equals("United States"))
    }
}