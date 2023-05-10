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
import com.example.myapplication.Exercise

class DBHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "exerciseDB2.db"

        private const val TABLE_EXERCISE = "ExerciseTable"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_UUID = "_UUID"
        private const val COLUMN_EXERCISE = "Name"
        private const val COLUMN_DATE = "Date"
        private const val COLUMN_WEIGHT = "Weight"
        private const val COLUMN_REPETITIONS = "Repetitions"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {
        // Define the SQL query for creating a table with the columns defined in the constants
        val CREATE_EXERCISE_TABLE =
            "CREATE TABLE $TABLE_EXERCISE($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_UUID TEXT, $COLUMN_EXERCISE TEXT, $COLUMN_DATE TEXT, $COLUMN_WEIGHT TEXT, $COLUMN_REPETITIONS TEXT)"

        // Execute the SQL query to create the table if the database is not null
        db?.execSQL(CREATE_EXERCISE_TABLE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // If the old version and new version don't match, drop the current table and create a new one
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE")
        onCreate(db)
    }

    fun addExercise(exercise: String, date: String, weight: Int, repetitions: Int) {
        // Create a new ContentValues object and add values to it
        val values = ContentValues()
        values.put(COLUMN_EXERCISE, exercise)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_WEIGHT, weight)
        values.put(COLUMN_REPETITIONS, repetitions)

        // Get a writable database and insert the values into the table
        val db = this.writableDatabase
        db.insert(TABLE_EXERCISE, null, values)
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
            /**
             * Retrieves a list of all exercises in the database.
             * Returns a LinkedList of Exercise objects.
             */
    fun getExercises(): LinkedList<Exercise> {
        // Create an empty LinkedList to hold the exercises
        val exercises: LinkedList<Exercise> = LinkedList()

        // Define the SQL query to select all rows from the exercise table
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE"

        // Get a readable reference to the database
        val db = this.readableDatabase

        // Execute the query and get a Cursor object
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Define variables to hold data from the Cursor
        var id: Int
        var uuid: String
        var exercise: String
        var date: String
        var weight: String
        var repetitions: String

        // Check if the Cursor contains any rows
        if (cursor.moveToFirst()) {
            // Loop through all rows in the Cursor
            do {
                // Retrieve data from the current row
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UUID))
                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
                repetitions = cursor.getString(cursor.getColumnIndex(COLUMN_REPETITIONS))

                // Create an Exercise object from the retrieved data
                val exerciseRecord = Exercise(id, uuid, exercise, date, weight, repetitions)

                // Add the Exercise object to the LinkedList
                exercises.add(exerciseRecord)

            } while (cursor.moveToNext())
        }

        // Close the database connection and the Cursor
        db.close()
        cursor.close()

        // Return the LinkedList of Exercise objects
        return exercises
    }

    @RequiresApi(Build.VERSION_CODES.O)
            /**
             * This function gets the position of a particular exercise in the list of all exercises.
             * It takes an integer ID parameter and returns an integer value representing the position.
             * The position is zero-indexed, so the first item in the list is at position 0.
             *
             * @param id an integer value representing the ID of the exercise to find the position of.
             * @return an integer value representing the position of the exercise in the list of all exercises.
             */
    fun getExercisePosition(id: Int): Int {
        // First, we retrieve a list of all exercises using the `getExercises()` function.
        val exercises: LinkedList<Exercise> = getExercises()

        // We initialize a variable `position` to 0, which will be incremented as we iterate through the list of exercises.
        var position: Int = 0

        // We loop through the list of exercises using a `for` loop.
        // For each exercise in the list, we check if its ID matches the `id` parameter we passed into the function.
        // If we find a match, we return the current value of `position`, which represents the position of the exercise in the list.
        // If we don't find a match, we increment the `position` variable and continue to the next exercise.
        for (exercise in exercises) {
            if (exercise.id == id) {
                return position
            }
            position++
        }

        // If we reach the end of the loop without finding a match, we return 0, which represents an invalid position.
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteExercise(exerciseRecord: Exercise) {
        // Open a writable database connection
        val db = this.writableDatabase

        // Choose the correct selection and selectionArgs based on whether the Exercise has an ID or UUID
        val selection = if (exerciseRecord.id != null) {
            "$COLUMN_ID=?"
        } else {
            "$COLUMN_UUID=?"
        }
        val selectionArgs = if (exerciseRecord.id != null) {
            arrayOf(exerciseRecord.id.toString())
        } else {
            arrayOf(exerciseRecord.uuid.toString())
        }

        // Use the delete method of the database object to delete the record
        db.delete(TABLE_EXERCISE, selection, selectionArgs)

        // Close the database connection
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
            /**
             * Update an existing exercise record in the database with new information.
             *
             * @param exerciseRecord The updated exercise record to be stored in the database.
             */
    fun updateExercise(exerciseRecord: Exercise) {
        // Open a writable database connection.
        val db = this.writableDatabase

        // Create a new ContentValues object to store the updated column values.
        val contentValues = ContentValues()
        contentValues.put(COLUMN_EXERCISE, exerciseRecord.exercise)
        contentValues.put(COLUMN_DATE, exerciseRecord.date)
        contentValues.put(COLUMN_WEIGHT, exerciseRecord.weight)
        contentValues.put(COLUMN_REPETITIONS, exerciseRecord.repetitions)

        // Use the update() method of SQLiteDatabase to update the row in the database with the
        // specified id.
        db.update(
            TABLE_EXERCISE, // The name of the table to update.
            contentValues, // The ContentValues object containing the updated column values.
            "$COLUMN_ID=?", // The selection criteria for the rows to update.
            arrayOf(exerciseRecord.id.toString()) // The selection criteria parameters.
        )

        // Close the database connection.
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
            /**
             * Updates an exercise record in the database with the specified values.
             *
             * @param id the id of the exercise record to update
             * @param uuid the uuid of the exercise record to update
             * @param exercise the new exercise name
             * @param date the new date of the exercise record
             * @param weight the new weight used for the exercise
             * @param repetitions the new number of repetitions performed for the exercise
             */
    fun updateExerciseRecord(
        id: Int,
        uuid: UUID,
        exercise: String,
        date: String,
        weight: String,
        repetitions: String )
    {
        // Open a writable database connection.
        val db = this.writableDatabase

        // Create a new ContentValues object to hold the updated values.
        val contentValues = ContentValues()

        // Put the updated values into the ContentValues object.
        contentValues.put(COLUMN_EXERCISE, exercise)
        contentValues.put(COLUMN_DATE, date)
        contentValues.put(COLUMN_WEIGHT, weight)
        contentValues.put(COLUMN_REPETITIONS, repetitions)

        // Update the exercise record in the database.
        // The COLUMN_ID=? and arrayOf(id.toString()) arguments specify the row to update based on the id.
        db.update(TABLE_EXERCISE, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))

        // Close the database connection.
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
            /**
             * Searches for exercises whose names start with the given query string.
             *
             * @param query the query string to search for
             * @return a linked list of exercises whose names start with the query string
             */
    fun searchExercise(query: String): LinkedList<Exercise> {
        // Initialize a linked list to store the search results.
        val exercises: LinkedList<Exercise> = LinkedList()

        // Construct the SQL select query to retrieve the rows whose exercise name starts with the query string.
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_EXERCISE LIKE \"$query%\""

        // Open a readable database connection.
        val db = this.readableDatabase

        // Execute the select query.
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Initialize variables to store the column values of the retrieved rows.
        var id: Int
        var uuid: String
        var exercise: String
        var date: String
        var weight: String
        var repetitions: String

        // Iterate through the rows returned by the query and extract their column values.
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UUID))
                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
                repetitions = cursor.getString(cursor.getColumnIndex(COLUMN_REPETITIONS))

                // Create a new Exercise object with the extracted column values and add it to the results list.
                val exerciseRecord = Exercise(id, uuid, exercise, date, weight, repetitions)
                exercises.add(exerciseRecord)

            } while (cursor.moveToNext())
        }

        // Close the database connection and the cursor.
        db.close()
        cursor.close()

        // Return the list of search results.
        return exercises
    }

    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    /**
     * Retrieves a list of exercises from the SQLite database ordered by date,
     * and returns only the latest exercise for each unique date.
     * If there are no exercises for a particular date, a default exercise with empty properties is added to the list.
     */
    fun getListOfElementsByDate(): List<Exercise> {

        // Create a map to store the exercises sorted by date
        val exercisesByDate: MutableMap<String, Exercise> = mutableMapOf()

        // SQL query to retrieve all exercises from the database ordered by date in descending order
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE ORDER BY $COLUMN_DATE DESC"

        // Open a readable database connection and execute the query
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Loop through each row returned by the query
        while (cursor.moveToNext()) {

            // Extract the values of each column for the current row
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID))
            val exerciseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

            // Create an Exercise object with the extracted values
            val exerciseRecord = Exercise(id, uuid, exerciseName, date, weight, repetitions)

            // Add the Exercise object to the map, using the UUID or exercise name as the key
            if (uuid != null && uuid.isNotBlank()) {
                exercisesByDate[uuid] = exerciseRecord
            } else {
                if (!exercisesByDate.containsKey(exerciseName)) {
                    exercisesByDate[exerciseName] = exerciseRecord
                }
            }
        }

        // Close the database connection and the cursor
        db.close()
        cursor.close()

        // Filter out duplicate exercise names, keeping the latest one
        val latestExercises = exercisesByDate.values.groupBy { it.exercise }
            .mapValues { (_, v) -> v.maxByOrNull { it.date }!! }

        // Create an empty list to store the latest exercises
        val result = mutableListOf<Exercise>()

        // Create a default exercise with empty properties
        val defaultExercise = Exercise(-1, "", "", "", "", "")

        // Add the latest exercise for each unique date to the list
        for (date in latestExercises.values.map { it.date }.distinct()) {
            val exercise = latestExercises.values.find { it.date == date } ?: defaultExercise
            result.add(exercise)
        }

        // Return the list of latest exercises
        return result
    }

    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    fun getRowById(id: Int): Exercise? {
        // Construct the SQL select query to retrieve the row based on the id.
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_ID = $id"

        // Open a readable database connection.
        val db = this.readableDatabase

        // Execute the select query.
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Initialize variables to store the column values of the retrieved row.
        var id: Int? = null
        var uuid: String? = null
        var exercise: String? = null
        var date: String? = null
        var weight: String? = null
        var repetitions: String? = null

        // If the cursor has a result, extract the column values and store them in the variables.
        if (cursor.moveToFirst()) {
            uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
        }

        // Close the database connection and the cursor.
        db.close()
        cursor.close()

        // Return a new Exercise object if all column values are not null, otherwise return null.
        return if (id != null && exercise != null && date != null && weight != null && repetitions != null) {
            Exercise(id, exercise, date, weight, repetitions)
        } else {
            null
        }
    }

        //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    fun getRowByUUID(uuid: UUID): Map<String, String>? {

        // Define the SELECT query with placeholders for arguments
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_UUID = ?"

        // Define the argument(s) for the query as an array
        val args = arrayOf(uuid.toString())

        // Get a readable database reference
        val db = this.readableDatabase

        // Execute the query and get a cursor that points to the result set
        val cursor = db.rawQuery(selectQuery, args)

        // Declare a variable to hold the row data as a Map<String, String> object
        var row: Map<String, String>? = null

        // Check if the cursor has any data
        if (cursor.moveToFirst()) {
            // If the cursor points to the first row of data, map the values to a new Map object
            row = mapOf(
                COLUMN_UUID to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID)),
                COLUMN_EXERCISE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE)),
                COLUMN_DATE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                COLUMN_WEIGHT to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                COLUMN_REPETITIONS to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
            )
        }

        // Close the database and cursor resources
        db.close()
        cursor.close()

        // Return the row data as a Map<String, String> object
        return row
    }

    /**
     * Retrieves an Exercise object from the database with the specified name and date
     * @param exerciseName the name of the exercise to retrieve
     * @param date the date of the exercise to retrieve
     * @return an Exercise object with the specified name and date, or null if not found
     */
    fun getExerciseByNameAndDate(exerciseName: String, date: String): Exercise? {
        val db = readableDatabase
        var exercise: Exercise? = null

        // Define the columns to retrieve from the database
        val columns = arrayOf(COLUMN_ID, COLUMN_UUID, COLUMN_EXERCISE, COLUMN_DATE, COLUMN_WEIGHT, COLUMN_REPETITIONS)

        // Define the selection criteria
        val selection = "$COLUMN_EXERCISE = ? AND $COLUMN_DATE = ?"

        // Define the selection arguments
        val selectionArgs = arrayOf(exerciseName, date)

        // Execute the query
        val cursor = db.query(TABLE_EXERCISE, columns, selection, selectionArgs, null, null, null)

        // Check if a row was returned
        if (cursor.moveToFirst()) {
            // Map the values from the row to an Exercise object
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val uuid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
            exercise = Exercise(id, uuid, name, date, weight, repetitions)
        }

        // Close the cursor and database connection
        cursor.close()
        db.close()

        // Return the Exercise object, or null if not found
        return exercise
    }




}