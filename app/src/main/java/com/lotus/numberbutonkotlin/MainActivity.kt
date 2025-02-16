package com.lotus.numberbutonkotlin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.lotus.numberbutonkotlin.databinding.ActivityMainBinding


class MainActivity :AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initViews()
    }


    private fun initViews() {

        checkNotNull(binding.numberErwachsene)

        binding.numberErwachsene.setBuyMax(20).setInventory(20).setCurrentNumber(0).setOnWarnListener(object :OnWarnListener {
            override fun onWarningForInventory(inventory: Int) {
                Toast.makeText(this@MainActivity, "Aktueller Counter:$inventory", Toast.LENGTH_SHORT).show()
            }

            override fun onWarningForBuyMax(max: Int) {
                Toast.makeText(this@MainActivity, "Limit wurde überschritten:$max", Toast.LENGTH_SHORT).show()
            }
        })


        checkNotNull(binding.numberButtonKinder)

        binding.numberButtonKinder.setBuyMax(10).setInventory(10).setCurrentNumber(0).setOnWarnListener(object :OnWarnListener {
            override fun onWarningForInventory(inventory: Int) {
                Toast.makeText(this@MainActivity, "Aktueller Counter:$inventory", Toast.LENGTH_SHORT).show()
            }

            override fun onWarningForBuyMax(max: Int) {
                Toast.makeText(this@MainActivity, "Sie haben Ihr Limit erreicht :$max", Toast.LENGTH_SHORT).show()
            }
        })

    }

}