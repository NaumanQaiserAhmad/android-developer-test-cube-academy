package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.di.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	@Inject
	lateinit var repository: Repository

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.createButton.setOnClickListener {
			val intent = Intent(this, CreateNominationActivity::class.java)
			startActivity(intent)
		}

		populateUI()
	}

	private fun populateUI() {
		/**
		 * TODO: Populate the UI with data in this function
		 * 		 You need to fetch the list of user's nominations from the api and put the data in the recycler view
		 * 		 And also add action to the "Create new nomination" button to go to the CreateNominationActivity
		 */
	}
}