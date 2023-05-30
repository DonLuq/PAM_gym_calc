package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.time.LocalDate

class PlotActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create an instance of your DBHandler class
        val dbHandler = DBHandler(this)

        // Retrieve data from the database
        val exercises = dbHandler.getExercises()

        // Log the size of the retrieved data
        Log.d("PlotActivity", "Number of exercises: ${exercises.size}")

        // Initialize total weight lifted and exercise count
        var totalWeightLifted = 0.0
        var exerciseCount = 0.0

        // Extract the data you want to plot and convert it to an array of DataPoint objects
        val progressDataPoints = mutableListOf<DataPoint>()

        for (exercise in exercises) {
            // Convert weight and repetitions to lists of Double
            val weightList = exercise.weight.split(" ").mapNotNull { it.toDoubleOrNull() }
            val repetitionsList = exercise.repetitions.split(" ").mapNotNull { it.toDoubleOrNull() }

            // If weight or repetitions is not numeric, show an error message and skip this exercise
            if (weightList.size != repetitionsList.size) {
                Log.e("PlotActivity", "Error: Mismatch in number of weights and repetitions for exercise ${exercise.id}")
            } else {
                val progress = weightList.zip(repetitionsList).sumByDouble { it.first * it.second }
                totalWeightLifted += progress
                exerciseCount++

                progressDataPoints.add(DataPoint(exerciseCount, totalWeightLifted))
            }
        }

        // Create LineGraphSeries using the sorted data points
        val progressSeries = LineGraphSeries(progressDataPoints.toTypedArray())

        // Create a GraphView, add the series to it and adjust the viewport
        val graphView = GraphView(this).apply {
            addSeries(progressSeries)
            title = "Progress"

            // Set viewport settings
            viewport.isScalable = true
            viewport.isScrollable = true
        }

        // Set the GraphView as the content view of the activity
        setContentView(graphView)
    }

}

