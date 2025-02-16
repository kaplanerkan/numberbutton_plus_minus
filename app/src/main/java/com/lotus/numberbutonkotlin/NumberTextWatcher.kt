package com.lotus.numberbutonkotlin

import android.text.Editable
import android.text.TextWatcher
import android.util.Log

class NumberTextWatcher(private val numberButton: NumberButton) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No action needed before text changed
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        numberButton.onNumberInput()
    }

    override fun afterTextChanged(s: Editable?) {
        // No action needed after text changed
        Log.e("NumberTextWatcher", "afterTextChanged: $s")
    }
}