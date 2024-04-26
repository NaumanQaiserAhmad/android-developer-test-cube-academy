package com.cube.cubeacademy.di

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.di.AppModule
import com.cube.cubeacademy.lib.di.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

class CustomTestRunner : AndroidJUnitRunner() {
	override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
		return super.newApplication(cl, HiltTestApplication::class.java.name, context)
	}
}

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
object AppTestModule {
	@Singleton
	@Provides
	fun provideApi(): ApiService = MockApiService()

	@Singleton
	@Provides
	fun provideMockApiService(): MockApiService = MockApiService()

	@Singleton
	@Provides
	fun provideRepository(api: ApiService): Repository = Repository(api)
}