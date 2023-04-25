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
    val uuid: UUID,
    val exercise: String,
    val date: String,
    val weight: String,
    val repetitions: String
)

class DBHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "exerciseDB.db"
        private const val TABLE_EXERCISE = "ExerciseTable"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_UUID = "_UUID"
        private const val COLUMN_EXERCISE = "Exercise"
        private const val COLUMN_DATE = "Date"
        private const val COLUMN_WEIGHT = "Weight"
        private const val COLUMN_REPETITIONS = "Repetitions"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_EXERCISE_TABLE =
            "CREATE TABLE $TABLE_EXERCISE($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_EXERCISE TEXT, $COLUMN_DATE TEXT, $COLUMN_WEIGHT TEXT, $COLUMN_REPETITIONS TEXT)"

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
        var uuid: UUID
        var exercise: String
        var date: String
        var weight: String
        var repetitions: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_UUID)))
                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
                repetitions = cursor.getString(cursor.getColumnIndex(COLUMN_REPETITIONS))

                val exerciseRecord = Exercise(id, uuid, exercise, date, weight, repetitions)
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
        uuid: UUID,
        exercise: String,
        date: String,
        weight: String,
        repetitions: String )
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
        var uuid: UUID
        var exercise: String
        var date: String
        var weight: String
        var repetitions: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_ID)))
                exercise = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
                repetitions = cursor.getString(cursor.getColumnIndex(COLUMN_REPETITIONS))

                val exerciseRecord = Exercise(id, uuid, exercise, date, weight, repetitions)
                exercises.add(exerciseRecord)

            } while (cursor.moveToNext())
        }

        db.close()
        cursor.close()
        return exercises
    }

    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    fun getListOfElementsByDate(): List<Exercise> {
        val exercisesByDate: MutableMap<String, Exercise> = mutableMapOf()

        val selectQuery = "SELECT * FROM $TABLE_EXERCISE ORDER BY $COLUMN_DATE DESC"

        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        var id: Int
        var uuid: UUID
        var exercise: String
        var date: String
        var weight: String
        var repetitions: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                uuid = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)))
                exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
                repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))

                val exerciseRecord = Exercise(id, uuid, exercise, date, weight, repetitions)

                if (!exercisesByDate.containsKey(date)) {
                    exercisesByDate[date] = exerciseRecord
                }

            } while (cursor.moveToNext())
        }

        db.close()
        cursor.close()
        return exercisesByDate.values.toList()
    }
    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    fun getRowById(id: Int): Exercise? {
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_ID = $id"

        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        var uuid: UUID? = null
        var exercise: String? = null
        var date: String? = null
        var weight: String? = null
        var repetitions: String? = null

        if (cursor.moveToFirst()) {
            uuid = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UUID)))
            exercise = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE))
            date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            weight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            repetitions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
        }

        db.close()
        cursor.close()

        return if (uuid != null && exercise != null && date != null && weight != null && repetitions != null) {
            Exercise(id, uuid, exercise, date, weight, repetitions)
        } else {
            null
        }
    }

    //Tutaj jest metoda getColumnIndexOrThrow zamiast GetColumnIndex poniewaz
    //wywala blad Value must be ≥ 0 but `getColumnIndex` can be -1. Nie wiem jak
    //to rozwiazac
    fun getRowByUUID(uuid: UUID): Map<String, String>? {
        val selectQuery = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_ID = ?"
        val args = arrayOf(uuid.toString())

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, args)

        var row: Map<String, String>? = null

        if (cursor.moveToFirst()) {
            row = mapOf(
                COLUMN_ID to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                COLUMN_EXERCISE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE)),
                COLUMN_DATE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                COLUMN_WEIGHT to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                COLUMN_REPETITIONS to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPETITIONS))
            )
        }

        db.close()
        cursor.close()
        return row
    }

}