package com.cube.cubeacademy.lib.models

import com.google.gson.annotations.SerializedName

data class Nomination(
	@SerializedName("nomination_id") val nominationId: String,
	@SerializedName("nominee_id") val nomineeId: String,
	@SerializedName("reason") val reason: String,
	@SerializedName("process") val process: String,
	@SerializedName("date_submitted") val dateSubmitted: String,
	@SerializedName("closing_date") val closingDate: String,
	@SerializedName("nominee_name") val nomineeName: String?
)