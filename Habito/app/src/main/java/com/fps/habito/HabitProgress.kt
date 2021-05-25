package com.fps.habito

class HabitProgress(private var progress: Int = 0, private val steps: Int, private var status: String = "NOT_STARTED") {

    override fun toString(): String {
        return "HabitProgress(progress=$progress, steps=$steps, status='$status')"
    }

}