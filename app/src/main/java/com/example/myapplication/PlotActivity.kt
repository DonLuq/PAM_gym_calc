package com.example.myapplication

import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DBHandler
import com.example.myapplication.R
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlotActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot)

        val graphView = findViewById<GraphView>(R.id.graph_view)

        val dbHandler = DBHandler(this)
        val exercises = dbHandler.getExercises()

        Log.d("PlotActivity", "Number of exercises: ${exercises.size}")

        val exerciseSeriesMap = mutableMapOf<String, MutableList<DataPoint>>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for (exercise in exercises) {
            val weightList = exercise.weight.split(" ").mapNotNull { it.toDoubleOrNull() }
            val repetitionsList = exercise.repetitions.split(" ").mapNotNull { it.toDoubleOrNull() }

            if (weightList.size != repetitionsList.size) {
                Log.e("PlotActivity", "Error: Mismatch in number of weights and repetitions for exercise ${exercise.id}")
            } else {
                val progress = weightList.zip(repetitionsList).sumByDouble { it.first * it.second }
                val exerciseDate = LocalDate.parse(exercise.date, formatter)
                val dataPoint = DataPoint(exerciseDate.toEpochDay().toDouble(), progress)
                exerciseSeriesMap.getOrPut(exercise.name) { mutableListOf() }.add(dataPoint)
            }
        }

        for ((type, dataPoints) in exerciseSeriesMap) {
            val sortedDataPoints = dataPoints.sortedBy { it.x }
            val movingAverageDataPoints = sortedDataPoints.windowed(5, 1) { window ->
                val yValues = window.map { it.y }
                DataPoint(window.first().x, yValues.sum() / window.size)
            }

            Log.d("PlotActivity", "Moving average data points for exercise type $type:")
            for (dataPoint in movingAverageDataPoints) {
                Log.d("PlotActivity", "Data Point: (${dataPoint.x}, ${dataPoint.y})")
            }

            val series = LineGraphSeries(movingAverageDataPoints.toTypedArray())
            series.title = type
            graphView.addSeries(series)
        }

        graphView.title = "Progress"
        graphView.viewport.isScalable = true
        graphView.viewport.isScrollable = true
        graphView.viewport.isXAxisBoundsManual = true
        graphView.viewport.isYAxisBoundsManual = true

        val minX = exerciseSeriesMap.values.flatten().minOf { it.x }
        val maxX = exerciseSeriesMap.values.flatten().maxOf { it.x }
        val xRangePadding = (maxX - minX) * 0.1
        val xMin = minX - xRangePadding
        val xMax = maxX + xRangePadding

        val minY = exerciseSeriesMap.values.flatten().minOf { it.y }
        val maxY = exerciseSeriesMap.values.flatten().maxOf { it.y }
        val yRangePadding = (maxY - minY) * 0.1
        val yMin = minY - yRangePadding
        val yMax = maxY + yRangePadding

        graphView.viewport.setMinX(xMin)
        graphView.viewport.setMaxX(xMax)
        graphView.viewport.setMinY(yMin)
        graphView.viewport.setMaxY(yMax)

        graphView.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    val localDate = LocalDate.ofEpochDay(value.toLong())
                    localDate.format(formatter)
                } else {
                    super.formatLabel(value, isValueX)
                }
            }
        }
    }
}
