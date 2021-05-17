package com.fps.habito

class Habit(
        var name: String,
        var desc: String,
        private var steps: Int = 1,
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
        return result
    }

    override fun toString(): String {
        return "Habit(name='$name', desc='$desc', steps=$steps, streak=$streak, allTime=$allTime, comp=$comp)"
    }


}