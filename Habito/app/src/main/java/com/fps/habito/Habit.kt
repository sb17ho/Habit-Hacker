package com.fps.habito

class Habit(
    private var name: String,
    private var desc: String,
    private var steps: Int,
    private var reminder: String,
    private var streak: Int = 0,
    private var allTime: Double = 0.0,
    private var comp: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Habit

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + steps
        result = 31 * result + reminder.hashCode()
        return result
    }

    override fun toString(): String {
        return "Habit(name='$name', desc='$desc', steps=$steps, reminder='$reminder', streak=$streak, allTime=$allTime, comp=$comp)"
    }


}