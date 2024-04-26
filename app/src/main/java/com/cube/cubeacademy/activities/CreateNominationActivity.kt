package com.cube.cubeacademy.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.adapters.NomineeAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.radioextension.RadioGroupExtension
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private lateinit var selectedRadio: String

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateNominationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //populateUI()

        // Launch a coroutine in a separate thread
        CoroutineScope(Dispatchers.IO).launch {
            val nominees = repository.getAllNominees()

            // Switch to the main thread to update UI
            withContext(Dispatchers.Main) {
                // Update UI or do any other operations with the nominees list
                populateUI(nominees)
            }
        }

        binding.nomineeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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

        binding.backButton.setOnClickListener {
            showBottomSheetDialog()
        }
        // submit button
        binding.submitButton.setOnClickListener {
            onSubmitButtonClick()
        }


        //Handeling Radio Buttons and Parent Linear Layout selectios
        mRadioGroupPlus = binding.radio
        mRadioGroupPlus.setOnCheckedChangeListener(object :
            RadioGroupExtension.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroupExtension?, checkedId: Int) {
                Log.i("RadioGroupPlus", "onCheckedChanged:")
                // Find the selected radio button using its ID
                val radioButton = findViewById<RadioButton>(checkedId)
                // Get the tag associated with the selected radio button
                selectedRadio = radioButton.tag.toString()
                checkConditionsAndEnableButton()
                resetBackgroundColors()

                val linearLayout = radioButton.parent as LinearLayout
                // Apply the border highlight to the LinearLayout
                linearLayout.setBackgroundResource(R.drawable.bg_dark_border)
            }
        })

        // Set OnClickListener to each LinearLayout
        for (i in 0 until mRadioGroupPlus.childCount) {
            val linearLayout = mRadioGroupPlus.getChildAt(i) as LinearLayout


            linearLayout.setOnClickListener {
                resetBackgroundColors()
                // Find the associated RadioButton using its tag
                val radioButtonTag = linearLayout.tag as String
                val radioButtonId = resources.getIdentifier("${radioButtonTag}_radio_button", "id", packageName)
                val radioButton = findViewById<RadioButton>(radioButtonId)

                // Check the RadioButton
                radioButton.isChecked = true
                linearLayout.setBackgroundResource(R.drawable.bg_dark_border)
            }
        }
    }

    private fun checkConditionsAndEnableButton() {
        // Check if Spinner has an item selected, EditText has some text, and a radio button is selected
        val isSpinnerItemSelected = binding.nomineeSpinner.selectedItem != null
        val isEditTextNotEmpty = binding.reasonEditText.text.toString().isNotEmpty()
        val isRadioButtonSelected = binding.radio.checkedRadioButtonId != -1

        // Enable the submit button if all conditions are met
        binding.submitButton.isEnabled =
            isSpinnerItemSelected && isEditTextNotEmpty && isRadioButtonSelected
    }


    private fun onSubmitButtonClick() {
        // Get the selected nominee from the Spinner
        val selectedNominee = binding.nomineeSpinner.selectedItem as? Nominee
        val nomineeId = selectedNominee?.nomineeId ?: ""

        // Get the reason from the EditText
        val reason = binding.reasonEditText.text.toString()

        // Get the selected radio button ID
        val checkedRadioButtonId = binding.radio.checkedRadioButtonId

        if (nomineeId.isNotBlank() && reason.isNotBlank() && checkedRadioButtonId != -1) {
            // Call the API function to create a nomination
            createNomination(nomineeId, reason, selectedRadio)
        } else {
            // Show an error message or handle the case where any of the required fields are empty
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNomination(nomineeId: String, reason: String, process: String) {
        // Launch a coroutine in a separate thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the repository function to create a nomination
                val nomination = repository.createNomination(nomineeId, reason, process)
                // Process the result if needed
                nomination?.let {
                    // Nomination created successfully
                    withContext(Dispatchers.Main) {
                        // Show a success message or navigate to another screen
                        Toast.makeText(
                            this@CreateNominationActivity,
                            "Nomination created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(
                            this@CreateNominationActivity,
                            NominationSubmittedActivity::class.java
                        )
                        startActivity(intent)
                    }
                } ?: run {
                    // Handle the case where nomination creation failed
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CreateNominationActivity,
                            "Failed to create nomination",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions such as network errors
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CreateNominationActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.onback_press_dialog, null)

        // Set up views and click listeners here if needed
        val cancelButton = view.findViewById<Button>(R.id.cacel)
        val leaveButton = view.findViewById<Button>(R.id.leave_button)

        cancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        leaveButton.setOnClickListener {
            // Used intent here instead of on back pressed because incase user comes here  from the submitted activty to create another
            //nominee it takes them to submitted actived on back rather then home.
            val intent = Intent(
                this@CreateNominationActivity,
                MainActivity::class.java
            )
            startActivity(intent)
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    // Function to reset background color of all LinearLayouts
    private fun resetBackgroundColors() {
        for (i in 0 until mRadioGroupPlus.childCount) {
            val linearLayout = mRadioGroupPlus.getChildAt(i) as LinearLayout
            linearLayout.setBackgroundResource(R.drawable.bg_reason_edittext)
        }
    }

}