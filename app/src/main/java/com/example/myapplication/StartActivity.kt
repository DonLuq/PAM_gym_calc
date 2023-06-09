package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Find the statistics button by its ID
        val statisticsButton: Button = findViewById(R.id.btn_stats)

        // Set a click listener for the statistics button
        statisticsButton.setOnClickListener {
            startPlotActivity()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save instance state here if needed
    }

    /**
     * Start the PlotActivity.
     */
    fun startPlotActivity() {
        val intent = Intent(this, PlotActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun pickDateMainActivity(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            var tempText: String = ""

            tempText += year.toString() + "-"

            if (month < 9) {
                tempText += "0" + (month + 1).toString() + "-"
            } else {
                tempText += (month + 1).toString() + "-"
            }

            if (day < 9) {
                tempText += "0" + day.toString()
            } else {
                tempText += day.toString()
            }

            val intent = Intent(this@StartActivity, MainActivity::class.java)
            intent.putExtra("DATE_FROM_PICK", tempText)
            startActivity(intent)

        }, year, month, day)

        dpd.show()
    }
}
