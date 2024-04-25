package com.cube.cubeacademy.lib.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cube.cubeacademy.lib.models.Nominee

class NomineeAdapter(context: Context, nominees: List<Nominee>) :
    ArrayAdapter<Nominee>(context, 0, nominees) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val nominee = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = nominee?.firstName // or nominee?.lastName if you prefer
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}