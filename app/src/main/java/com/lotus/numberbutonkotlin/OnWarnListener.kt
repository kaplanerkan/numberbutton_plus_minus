package com.lotus.numberbutonkotlin

interface OnWarnListener {
    fun onWarningForInventory(inventory: Int)
    fun onWarningForBuyMax(max: Int)
}