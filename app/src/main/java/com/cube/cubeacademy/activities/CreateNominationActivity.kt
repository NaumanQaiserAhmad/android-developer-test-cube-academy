package com.cube.cubeacademy.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.adapters.NomineeAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.radioextension.RadioGroupExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
	private lateinit var binding: ActivityCreateNominationBinding
	private lateinit var mRadioGroupPlus: RadioGroupExtension

	@Inject
	lateinit var repository: Repository

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCreateNominationBinding.inflate(layoutInflater)
		setContentView(binding.root)

		//populateUI()

		mRadioGroupPlus = binding.radio
		mRadioGroupPlus.setOnCheckedChangeListener(object : RadioGroupExtension.OnCheckedChangeListener {
			override fun onCheckedChanged(group: RadioGroupExtension?, checkedId: Int) {
				Log.i("RadioGroupPlus", "onCheckedChanged:")
				checkConditionsAndEnableButton()
			}
		})

		// Launch a coroutine in a separate thread
		CoroutineScope(Dispatchers.IO).launch {
			val nominees = repository.getAllNominees()

			// Switch to the main thread to update UI
			withContext(Dispatchers.Main) {
				// Update UI or do any other operations with the nominees list
				populateUI(nominees)
			}
		}

		binding.nomineeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
				val selectedNominee = parent.getItemAtPosition(position)
				//Log.d("Spinner", "Selected nominee: $selectedNominee")
				// Check conditions and enable the submit button
				checkConditionsAndEnableButton()
			}

			override fun onNothingSelected(parent: AdapterView<*>) {
				// Handle case where nothing is selected (if needed)
			}
		}

		// Set up TextChangedListener for the EditText
		binding.reasonEditText.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
				// Not needed
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				// Check conditions and enable the submit button
				checkConditionsAndEnableButton()
			}

			override fun afterTextChanged(s: Editable?) {
				// Not needed
			}
		})

	}

	private fun populateUI(nominees: List<Nominee>) {
		/**
		 * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
		 * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
		 * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
		 */
		Log.v("nominee", nominees.toString())

		// Assuming you have already fetched the list of nominees and stored it in a variable called 'nominees'
		val adapter = NomineeAdapter(this, nominees)
		binding.nomineeSpinner.adapter = adapter
	}

	private fun checkConditionsAndEnableButton() {
		// Check if Spinner has an item selected, EditText has some text, and a radio button is selected
		val isSpinnerItemSelected = binding.nomineeSpinner.selectedItem != null
		val isEditTextNotEmpty = binding.reasonEditText.text.toString().isNotEmpty()
		val isRadioButtonSelected = binding.radio.checkedRadioButtonId != -1

		// Enable the submit button if all conditions are met
		binding.submitButton.isEnabled = isSpinnerItemSelected && isEditTextNotEmpty && isRadioButtonSelected
	}

}