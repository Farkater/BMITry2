package com.example.bmitry2


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmitry2.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private lateinit var weight_history_button: Button
        private lateinit var save_button: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)


            weight_history_button = findViewById((R.id.weight_history_button))
            save_button = findViewById(R.id.save_button)
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

                // Update the TextViews with the saved values
                if (bmi != "") {
                    binding.resultsTxt.text = String.format("Your BMI is %s", bmi)
                }
                if (healthStatus != "") {
                    binding.healthyTxt.text = String.format("Considered: %s", healthStatus)
                }

                binding.weight.setOnValueChangedListener { _, _, _ ->
                    calculateBMI()
                }
                binding.height.setOnValueChangedListener { _, _, _ ->
                    calculateBMI()
                }
            }


            save_button.setOnClickListener {
                val dbHelper = DBHelper(this)
                val weight = binding.weight.value.toString() //get the value from the weight picker
                val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()) //get the current date
                val newRowId = dbHelper.insertData(weight, currentDate)
                if (newRowId != -1L) {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error inserting data", Toast.LENGTH_SHORT).show()
                }
            }





            weight_history_button.setOnClickListener {
                val dbHelper = DBHelper(this)
                val intent = Intent(this, weight_history::class.java)
                startActivity(intent)
                val db = dbHelper.readableDatabase

                val projection = arrayOf(
                    DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT,
                    DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DATA
                )

                val cursor = db.query(
                    DBHelper.FeedReaderContract.FeedEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
                )

                val weightList = ArrayList<String>()
                while (cursor.moveToNext()) {
                    val weight = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT))
                    val data = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DATA))
                    weightList.add("Weight: $weight , Date: $data")
                }
                cursor.close()

                val listView = findViewById<ListView>(R.id.list_view)
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weightList)
                listView.adapter = adapter
            }




        }

        private fun calculateBMI() {
            val height = binding.height.value
            val dHeight = height.toDouble() / 100
            val weight = binding.weight.value
            val bmi = weight.toDouble() / (dHeight * dHeight)

            val bmiRounded = BigDecimal(bmi).setScale(2, RoundingMode.HALF_UP)
            binding.resultsTxt.text = String.format("Your BMI is %s", bmiRounded)
            binding.healthyTxt.text = String.format("Considered: %s", bmiHealth(bmi))

            // Save the calculated BMI, health status, weight, and height to Shared Preferences
            val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("bmi", bmiRounded.toString())
            editor.putString("health_status", bmiHealth(bmi))
            editor.putInt("weight", weight)
            editor.putInt("height", height)
            editor.apply()
        }



        private fun bmiHealth(bmi: Double): String {
            if (bmi < 18.5)
                return "Underweight"
            if (bmi < 25.0)
                return "Healthy"
            if (bmi < 30)
                return "Overweight"

            return "Invalid"
        }





    }

