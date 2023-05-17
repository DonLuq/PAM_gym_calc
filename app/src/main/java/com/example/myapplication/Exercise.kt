 package com.example.myapplication;

 import android.os.Build;

 import androidx.annotation.RequiresApi;

 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import java.util.Date;
 import java.util.UUID;

// Data class for Exercise
data class Exercise(
    var id: Int,
    var uuid: String = UUID.randomUUID().toString(),
    var exercise: String,
    var date: String = "2000-12-12 13:20:00",
    var weight: String = "10 10 10",
    var repetitions: String = "15 15 15"
) {
    // Constructor for Exercise
    constructor(exercise: Exercise) : this(
        exercise.id,
        exercise.uuid ?: UUID.randomUUID().toString(),
        exercise.exercise,
        exercise.date,
        exercise.weight,
        exercise.repetitions
    )
     // Setter for Id
//    fun setId(Id: Int) {
//        id = Id
//    }
//     // Getter for Id
//    fun getId(): Int {
//        return id
//    }
//     // Setter for UUID
//    fun setUUID(UUID: String?) {
//        if (UUID != null) {
//            uuid = UUID
//        }
//    }
//     // Getter for UUID
//    fun getUUID(): String? {
//        return uuid
//    }
//     // Setter for Exercise
//    fun setExercise(e: String?) {
//        if (e != null) {
//            exercise = e
//        }
//    }
//     // Getter for Exercise
//    fun getExercise(): String? {
//        return exercise
//    }
//     // Setter for Date
//    fun setDate(s: String?) {
//        if (s != null) {
//            date = s
//        }
//    }
//     // Getter for Date
//    fun getDate(): String? {
//        return date
//    }
//     // Setter for Weight
//    fun setWeight(w: String?) {
//        if (w != null) {
//            weight = w
//        }
//    }
//     // Getter for Weight
//    fun getWeight(): String? {
//        return weight
//    }
//     // Setter for Repetitions
//    fun setRepetitions(r: String?) {
//        if (r != null) {
//            repetitions = r
//        }
//    }
//     // Getter for Repetitions
//    fun getRepetitions(): String? {
//        return repetitions
//    }
}