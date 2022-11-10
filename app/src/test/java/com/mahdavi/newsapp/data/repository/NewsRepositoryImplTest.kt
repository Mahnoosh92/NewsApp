package com.mahdavi.newsapp.data.repository

import org.junit.After
import org.junit.Before
import org.junit.Test


internal class NewsRepositoryImplTest {

    private lateinit var newsRepository: NewsRepository

    @Before
    fun setup() {
        newsRepository = FakeNewsRepositoryImpl()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `should return exception`() {

    }
}