package com.example.uniconnect

import com.example.uniconnect.dto.Post
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UniConnectUnitTest {
    @Test
    fun `Given a post DTO when title is CCM Concert and description is In Corbet Auditorium `(){
        var post = Post("CCM Concert", "In Corbet Auditorium")
        assertTrue(post.title.equals("CCM Concert"))
        assertTrue(post.description.equals("In Corbet Auditorium"))
    }
    @Test
    fun `Given a post DTO when  title is CCM Concert and description is In Corbet Auditorium then output is CCM Concert - In Corbet Auditorium`(){
        var post = Post("CCM Concert", "In Corbet Auditorium")
        assertTrue(post.toString().equals("CCM Concert - In Corbet Auditorium"))
    }

}
