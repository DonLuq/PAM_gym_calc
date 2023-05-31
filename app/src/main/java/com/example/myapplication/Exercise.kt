package com.example.myapplication

import java.util.UUID

/**
 * Data class representing an Exercise.
 *
 * @property id The unique identifier of the exercise.
 * @property uuid The UUID (Universally Unique Identifier) of the exercise.
 * @property name The name of the exercise.
 * @property date The date of the exercise.
 * @property weight The weight used for the exercise.
 * @property repetitions The number of repetitions performed for the exercise.
 */
data class Exercise(
    var id: Int,
    var uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var date: String = "",
    var weight: String = "",
    var repetitions: String = ""
) {
    /**
     * Secondary constructor for Exercise.
     *
     * @param exercise The exercise object to copy the values from.
     */
    constructor(exercise: Exercise) : this(
        exercise.id,
        exercise.uuid ?: UUID.randomUUID().toString(),
        exercise.name,
        exercise.date,
        exercise.weight,
        exercise.repetitions
    )

    /**
     * Property that returns the number of days in the date field.
     */
    val daysCount: Int
        get() = date.split("-").size

    /**
     * Property that calculates the total weight lifted for the exercise.
     */
    val totalWeight: Int
        get() {
            // Assuming weight and repetitions are space-separated lists of integers
            val weights = weight.split(" ").map { it.toInt() }
            val reps = repetitions.split(" ").map { it.toInt() }

            return weights.zip(reps).sumBy { (weight, rep) -> weight * rep }
        }
}
