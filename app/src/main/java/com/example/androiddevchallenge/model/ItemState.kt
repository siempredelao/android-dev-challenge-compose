package com.example.androiddevchallenge.model

enum class State {
    RECIPE, CONFIRMATION
}

data class ItemState(val recipe: Recipe, val state: State)