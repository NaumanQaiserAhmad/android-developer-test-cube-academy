package com.cube.cubeacademy.lib.di

import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee

class Repository(val api: ApiService) {
	// TODO: Add additional code if you need it

	suspend fun getAllNominations(): List<Nomination> {
		// Fetch the list of nominations from the API
		val dataWrapper = api.getAllNominations()
		return dataWrapper.data ?: emptyList()
	}

	suspend fun getAllNominees(): List<Nominee> {
		// Fetch the list of all nominees from the API
		val dataWrapper = api.getAllNominees()
		return dataWrapper.data ?: emptyList()
	}

	suspend fun createNomination(nomineeId: String, reason: String, process: String): Nomination? {
		// Create a new nomination using the API
		val dataWrapper = api.createNomination(nomineeId, reason, process)
		return dataWrapper.data
	}
}
