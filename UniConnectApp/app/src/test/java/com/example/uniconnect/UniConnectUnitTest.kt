package com.example.uniconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.University
import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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

    lateinit var mvm : MainViewModel

    @MockK
    lateinit var mockUniService : UniversityService

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var universityService: IUniversityService
    var allUniversities : List<University>? = ArrayList<University>()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun populateUniversities() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `Given a post DTO when title is CCM Concert and description is In Corbet Auditorium `(){
        val post = Post("","CCM Concert", "In Corbet Auditorium")
        assertTrue(post.title.equals("CCM Concert"))
        assertTrue(post.description.equals("In Corbet Auditorium"))
    }
    @Test
    fun `Given a post DTO when  title is CCM Concert and description is In Corbet Auditorium then output is CCM Concert - In Corbet Auditorium`(){
        val post = Post("","CCM Concert", "In Corbet Auditorium")
        assertTrue(post.toString().equals("CCM Concert - In Corbet Auditorium"))
    }

    @Test
    fun `Given a University DTO when name is MaryWood University and country is United States`(){
        val university = University("MaryWood University", "United States", "US")
        assertTrue(university.name.equals("MaryWood University"))
        assertTrue(university.country.equals("United States"))
    }

    @Test
    fun `Given service connects to University JSON stream when data are read and parsed then university collection should be greater than zero`() =
        runTest {
        launch(Dispatchers.Main) {
            givenUniversityServiceIsInitialized()
            whenServiceDataAreReadAndParsed()
            thenTheUniversityCollectionSizeShouldBeGreaterThanZero()
        }
    }

    private fun givenUniversityServiceIsInitialized() {
        universityService = UniversityService()
    }

    private suspend fun whenServiceDataAreReadAndParsed() {
        allUniversities = universityService.fetchUniversities()
    }

    private fun thenTheUniversityCollectionSizeShouldBeGreaterThanZero() {
        assertNotNull(allUniversities)
        assertTrue(allUniversities!!.isNotEmpty())
    }

    @Test
    fun `Given a viewModel with live data when populated with univeristies results should show University Of Cincinnati`(){
        viewModelInitializedWithMockData()
        jsonDataIsParasedAndRead()
        thenResultsShouldContainUOfCinci()
    }

    private fun viewModelInitializedWithMockData() {
        val universities = ArrayList<University>()
        universities.add(University("University of Cincinnati", "United States", "US"))
        universities.add(University("Xavier University", "United States", "US"))
        universities.add(University("University of Cambridge", "United Kingdom", "GB"))

        coEvery{ mockUniService.fetchUniversities()} returns universities
        mvm.universityService = mockUniService
    }

    private fun jsonDataIsParasedAndRead() {
        mvm.fetchUniversities()
    }

    private fun thenResultsShouldContainUOfCinci() {
        var allUni : List<University>? = ArrayList<University>()
        val latch = CountDownLatch(1);
        val observer = object : Observer<List<University>> {
            override fun onChanged(x: List<University>?) {
                allUni = x
                latch.countDown()
                mvm.universities.removeObserver(this)
            }
        }
        mvm.universities.observeForever(observer)

        latch.await(1, TimeUnit.SECONDS)
        assertNotNull(allUni)
        assertTrue(allUni!!.contains(University("University of Cincinnati", "United States", "US")))
    }

}
