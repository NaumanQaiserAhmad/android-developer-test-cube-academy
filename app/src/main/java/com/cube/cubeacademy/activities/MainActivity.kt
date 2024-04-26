package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.adapters.NominationsRecyclerViewAdapter
import com.cube.cubeacademy.lib.di.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        populateUI()
    }

    private fun populateUI() {
        /**
         * TODO: Populate the UI with data in this function
         * 		 You need to fetch the list of user's nominations from the api and put the data in the recycler view
         * 		 And also add action to the "Create new nomination" button to go to the CreateNominationActivity
         */

        binding.createButton.setOnClickListener {
            val intent = Intent(this, CreateNominationActivity::class.java)
            startActivity(intent)
        }

        // Launch a coroutine in a separate thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch the list of nominations from the repository
                val nominations = repository.getAllNominations()
                val nominees = repository.getAllNominees()

                // Create a map to store nominee names using nomineeId as the key
                val nomineeMap = nominees.associateBy { it.nomineeId }

                val nominationsWithFullName = nominations.map { nomination ->
                    val nominee = nomineeMap[nomination.nomineeId]
                    val fullName =
                        nominee?.let { "${it.firstName} ${it.lastName}" } ?: nomination.nomineeName
                    nomination.copy(nomineeName = fullName)
                }

                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    // Check if the fetched list is not empty
                    if (nominations.isNotEmpty()) {
                        // Create an adapter for the RecyclerView and pass the repository instance
                        val adapter = NominationsRecyclerViewAdapter()

                        // Set the adapter for the RecyclerView
                        binding.nominationsList.adapter = adapter

                        // Submit the list of nominations to the adapter
                        adapter.submitList(nominationsWithFullName)

                        // Make the RecyclerView visible
                        binding.nominationsList.visibility = View.VISIBLE
                        binding.emptyContainer.visibility = View.GONE
                    } else {
                        binding.emptyContainer.visibility = View.VISIBLE
                        // Handle the case where the fetched list is empty
                        // For example, display a message indicating no nominations found
                        // You can also hide or keep the RecyclerView invisible in this case
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions such as network errors
                // For example, display an error message or log the exception
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
