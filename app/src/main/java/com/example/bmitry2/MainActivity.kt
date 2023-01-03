package com.example.bmitry2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bmitry2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weight.minValue = 50
        binding.weight.maxValue = 200

        binding.height.minValue = 75
        binding.height.maxValue = 220

        // Retrieve the saved values from Shared Preferences
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val weight = sharedPreferences.getInt("weight", 0)
        val height = sharedPreferences.getInt("height", 0)
        val bmi = sharedPreferences.getString("bmi", "")
        val healthStatus = sharedPreferences.getString("health_status", "")

        // Set the values of the NumberPickers to the saved values
        if (weight != 0) {
            binding.weight.value = weight
        }
        if (height != 0) {
            binding.height.value = height
        }

        // Update the TextViews with the saved values
        if (bmi != "") {
            binding.resultsTxt.text = String.format("Your BMI is %s", bmi)
        }
        if (healthStatus != "") {
            binding.healthyTxt.text = String.format("Considered: %s", healthStatus)
        }

        binding.weight.setOnValueChangedListener{ _,_,_ ->
            calculateBMI()
        }
        binding.height.setOnValueChangedListener{ _,_,_ ->
            calculateBMI()
        }
    }

    private fun calculateBMI() {
        val height = binding.height.value
        val dHeight = height.toDouble() / 100
        val weight = binding.weight.value
        val bmi = weight.toDouble() / (dHeight * dHeight)

        binding.resultsTxt.text = String.format("Your BMI is %.2f", bmi)
        binding.healthyTxt.text = String.format("Considered: %s",bmiHealth(bmi))

        // Save the calculated BMI, health status, weight, and height to Shared Preferences
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("bmi", bmi.toString())
        editor.putString("health_status", bmiHealth(bmi))
        editor.putInt("weight", weight)
        editor.putInt("height", height)
        editor.apply()
    }

    private fun bmiHealth(bmi: Double): String {
        if(bmi <18.5)
            return "Underweight"
        if(bmi <25.0)
            return "Healthy"
        if(bmi <30)
            return "Overweight"

        return "Invalid"
    }
}
