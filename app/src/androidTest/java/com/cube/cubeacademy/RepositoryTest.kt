package com.cube.cubeacademy

import com.cube.cubeacademy.di.MockApiService
import com.cube.cubeacademy.lib.di.Repository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest

class RepositoryTest {
	@get:Rule
	var hiltRule = HiltAndroidRule(this)

	@Inject
	lateinit var repository: Repository

	@Inject
	lateinit var mockApiService: MockApiService

	@Before
	fun setUp() {
		hiltRule.inject()
	}

	@Test
	fun testRepositoryInjection() {
		// Ensure that the repository is injected properly
		assertNotNull(repository)
	}

	@Test
	fun getNominationsTest() {
		// TODO: Write a test for getting all the nominations from the mock api
		runBlocking {

			val expectedNominationCount = 4
			val expectedNomineeIds = listOf("1", "2", "3", "4")


			val nominations = repository.getAllNominations()

			// Then
			assertNotNull(nominations)
			assertEquals(expectedNominationCount, nominations.size)
			for (nomination in nominations) {
				assertTrue(expectedNomineeIds.contains(nomination.nomineeId))
			}
		}

	}

	@Test
	fun getNomineesTest() {
		runBlocking {

			val expectedNomineeCount = 3

			val nominees = repository.getAllNominees()

			// Check
			assertNotNull(nominees)
			assertEquals(expectedNomineeCount, nominees.size)
		}
	}

	@Test
	fun createNominationTest() {
		runBlocking {

			val nomineeId = "5"
			val reason = "Test reason"
			val process = "Test process"


			val createdNomination = repository.createNomination(nomineeId, reason, process)

			// Check
			assertNotNull(createdNomination)
			assertEquals(nomineeId, createdNomination?.nomineeId)
			assertEquals(reason, createdNomination?.reason)
			assertEquals(process, createdNomination?.process)
		}
	}
}