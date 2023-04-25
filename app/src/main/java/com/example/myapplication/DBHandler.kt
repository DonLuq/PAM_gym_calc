package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

data class Exercise(
    val id: Int,
    val exercise: String,
    val date: String,
    val weight: Int,
    val repetitions: Int
)

class DBHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "exerciseDB.db"
        private const val TABLE_EXERCISE = "ExerciseTable"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_EXERCISE = "Exercise"
        private const val COLUMN_DATE = "Date"
        private const val COLUMN_WEIGHT = "Weight"
        private const val COLUMN_REPETITIONS = "Repetitions"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_EXERCISE_TABLE =
            "CREATE TABLE $TABLE_EXERCISE($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_EXERCISE TEXT, $COLUMN_DATE TEXT, $COLUMN_WEIGHT INTEGER, $COLUMN_REPETITIONS INTEGER)"

        db?.execSQL(CREATE_EXERCISE_TABLE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE")
        onCreate(db)
    }

    fun addExercise(exercise: String, date: String, weight: Int, repetitions: Int) {
        val values = ContentValues()
        values.put(COLUMN_EXERCISE, exercise)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_WEIGHT, weight)
        values.put(COLUMN_REPETITIONS, repetitions)

        val db = this.writableDatabase
        db.insert(TABLE_EXERCISE, null, values)
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun getExercises(): LinkedList<Exercise> {
        val exercises: LinkedList<Exercise> = LinkedList()
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE"

        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        var id: Int
        var exercise: String
        var date: String
        var weight: Int
        var repetitions: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                weight = cursor.getInt(cursor.getColumnIndex(COLUMN_WEIGHT))
                repetitions = cursor.getInt(cursor.getColumnIndex(COLUMN_REPETITIONS))

                val exerciseRecord = Exercise(id, exercise, date, weight, repetitions)
                exercises.add(exerciseRecord)

            } while (cursor.moveToNext())
        }

        db.close()
        cursor.close()
        return exercises
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExercisePosition(id: Int): Int {
        val exercises: LinkedList<Exercise> = getExercises()

        var position: Int = 0
        for (exercise in exercises) {
            if (exercise.id == id) {
                return position
            }
            position++
        }
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteExercise(exerciseRecord: Exercise) {
        val db = this.writableDatabase
        db.delete(TABLE_EXERCISE, "$COLUMN_ID=?", arrayOf(exerciseRecord.id.toString()))
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExercise(exerciseRecord: Exercise) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_EXERCISE, exerciseRecord.exercise)
        contentValues.put(COLUMN_DATE, exerciseRecord.date)
        contentValues.put(COLUMN_WEIGHT, exerciseRecord.weight)
        contentValues.put(COLUMN_REPETITIONS, exerciseRecord.repetitions)

        db.update(
            TABLE_EXERCISE,
            contentValues,
            "$COLUMN_ID=?",
            arrayOf(exerciseRecord.id.toString())
        )
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateExerciseRecord(
        id: Int,
        exercise: String,
        date: String,
        weight: Int,
        repetitions: Int )
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_EXERCISE, exercise)
        contentValues.put(COLUMN_DATE, date)
        contentValues.put(COLUMN_WEIGHT, weight)
        contentValues.put(COLUMN_REPETITIONS, repetitions)

        db.update(TABLE_EXERCISE, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun searchExercise(query: String): LinkedList<Exercise> {
        val exercises: LinkedList<Exercise> = LinkedList()
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_EXERCISE LIKE \"$query%\""

        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        var id: Int
        var exercise: String
        var date: String
        var weight: Int
        var repetitions: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                weight = cursor.getInt(cursor.getColumnIndex(COLUMN_WEIGHT))
                repetitions = cursor.getInt(cursor.getColumnIndex(COLUMN_REPETITIONS))

                val exerciseRecord = Exercise(id, exercise, date, weight, repetitions)
                exercises.add(exerciseRecord)

            } while (cursor.moveToNext())
        }

        db.close()
        cursor.close()
        return exercises
    }

}