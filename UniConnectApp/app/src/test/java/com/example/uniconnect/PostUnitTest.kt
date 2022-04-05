package com.example.uniconnect

import com.example.uniconnect.dto.Post
import org.junit.Assert
import org.junit.Test

class PostUnitTest {
    @Test
    fun `Given a post DTO when title is CCM Concert and description is In Corbet Auditorium `() {
        val post = Post("", "CCM Concert", "In Corbet Auditorium")
        Assert.assertTrue(post.title.equals("CCM Concert"))
        Assert.assertTrue(post.description.equals("In Corbet Auditorium"))
    }

    @Test
    fun `Given a post DTO when  title is CCM Concert and description is In Corbet Auditorium then output is CCM Concert - In Corbet Auditorium`() {
        val post = Post("", "CCM Concert", "In Corbet Auditorium")
        Assert.assertTrue(post.toString().equals("CCM Concert - In Corbet Auditorium"))
    }
}