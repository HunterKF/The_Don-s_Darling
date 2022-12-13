package com.example.loveletter.util

class Tools {
    companion object {
        fun randomNumber(size: Int): Int {
            return (1..size).shuffled().random()
        }
    }
}