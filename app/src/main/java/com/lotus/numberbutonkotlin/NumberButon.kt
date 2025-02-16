package com.lotus.numberbutonkotlin

import android.content.Context
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.lotus.numberbutonkotlin.databinding.CustomWidgetNumberButtonBinding
import java.util.Locale


class NumberButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mInventory = Int.MAX_VALUE
    private var mBuyMax = Int.MAX_VALUE
    private var mOnWarnListener: OnWarnListener? = null
    private val binding: CustomWidgetNumberButtonBinding = CustomWidgetNumberButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding.buttonAdd.setOnClickListener(this)
        binding.buttonSub.setOnClickListener(this)
        binding.textCount.addTextChangedListener(NumberTextWatcher(this))
        binding.textCount.setOnClickListener(this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberButton)
        val editable = typedArray.getBoolean(R.styleable.NumberButton_editable, true)
        val buttonWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_buttonWidth, -1)
        val textWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_textWidth, -1)
        val textSize = typedArray.getDimensionPixelSize(R.styleable.NumberButton_textSize, -1)
        val textColor = typedArray.getColor(R.styleable.NumberButton_textColor, 0xff000000.toInt())
        typedArray.recycle()

        setEditable(editable)
        binding.textCount.setTextColor(textColor)
        binding.buttonSub.setTextColor(textColor)
        binding.buttonAdd.setTextColor(textColor)

        if (textSize > 0) binding.textCount.textSize = textSize.toFloat()

        if (buttonWidth > 0) {
            val textParams = LayoutParams(buttonWidth, LayoutParams.MATCH_PARENT)
            binding.buttonSub.layoutParams = textParams
            binding.buttonAdd.layoutParams = textParams
        }
        if (textWidth > 0) {
            val textParams = LayoutParams(textWidth, LayoutParams.MATCH_PARENT)
            binding.textCount.layoutParams = textParams
        }
    }

    private val number: Int
        get() = try {
            binding.textCount.text.toString().toInt()
        } catch (e: NumberFormatException) {
            binding.textCount.setText("0")
            0
        }

    override fun onClick(v: View) {
        val id = v.id
        val count = number
        when (id) {
            R.id.button_sub -> if (count > 0) {
                //binding.textCount.setText((count - 1).toString())
                binding.textCount.setText(String.format(Locale.getDefault(), "%02d", count - 1))
            }

            R.id.button_add -> {
                // Depraced
                // if (count < Math.min(mBuyMax, mInventory)) {
                if (count < mBuyMax.coerceAtMost(mInventory)) {
                    //binding.textCount.setText((count + 1).toString())
                    binding.textCount.setText(String.format(Locale.getDefault(), "%02d", count + 1))

                } else if (mInventory < mBuyMax) {
                    warningForInventory()
                } else {
                    warningForBuyMax()
                }
            }

            R.id.text_count -> binding.textCount.setSelection(binding.textCount.text.toString().length)
        }
    }

    fun onNumberInput() {
        val count = number
        if (count <= 0) {
           // binding.textCount.setText("0")
            return
        }

        // val limit = Math.min(mBuyMax, mInventory)
        val limit = mBuyMax.coerceAtMost(mInventory)
        if (count > limit) {
            //binding.textCount.setText(limit.toString())
            binding.textCount.setText(String.format(Locale.getDefault(), "%02d", limit))

            if (mInventory < mBuyMax) {
                warningForInventory()
            } else {
                warningForBuyMax()
            }
        }
    }

    private fun warningForInventory() {
        mOnWarnListener?.onWarningForInventory(mInventory)
    }

    private fun warningForBuyMax() {
        mOnWarnListener?.onWarningForBuyMax(mBuyMax)
    }

    private fun setEditable1(editable: Boolean) {
        if (editable) {
            binding.textCount.isFocusable = true
            binding.textCount.keyListener = DigitsKeyListener.getInstance("0123456789")
        } else {
            binding.textCount.isFocusable = false
            binding.textCount.keyListener = null
        }
    }

    private fun setEditable(editable: Boolean) {
        binding.textCount.isFocusable = editable
        binding.textCount.keyListener = if (editable) DigitsKeyListener.getInstance("0123456789") else null
    }


    fun setCurrentNumber(currentNumber: Int): NumberButton {
        if (currentNumber <=0) {
            try {
                binding.textCount.setText("0")
            } catch (e: Exception) {
                Log.e("NumberButton", "setCurrentNumber: $e")
            }

        }
        // Depraced

        //     binding.textCount.setText("" + Math.min(Math.min(mBuyMax, mInventory), currentNumber))
        val step1 = mBuyMax.coerceAtMost(mInventory)
        val step2 = step1.coerceAtMost(currentNumber)
        try {
            binding.textCount.setText(String.format(Locale.getDefault(), "%02d", step2))
        } catch (e: Exception) {
            binding.textCount.setText("0")
        }

        return this
    }

    fun setInventory(inventory: Int): NumberButton {
        mInventory = inventory
        return this
    }

    fun setBuyMax(buyMax: Int): NumberButton {
        mBuyMax = buyMax
        return this
    }

    fun setOnWarnListener(onWarnListener: OnWarnListener): NumberButton {
        mOnWarnListener = onWarnListener
        return this
    }

}