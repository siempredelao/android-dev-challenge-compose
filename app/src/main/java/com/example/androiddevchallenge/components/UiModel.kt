package com.example.androiddevchallenge.components

import com.example.androiddevchallenge.model.Recipe

data class UiModel(val list: List<Recipe>, val totalPrice: Double) {
    fun isEmpty() = list.isEmpty()
}