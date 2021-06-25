package com.fps.habito

class User(val userName: String, val userHabits: MutableList<Habit>) {
    constructor() : this("", ArrayList())
}
