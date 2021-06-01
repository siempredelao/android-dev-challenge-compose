package com.example.androiddevchallenge.components

import com.example.androiddevchallenge.model.ItemState

data class UiStateModel(val list: List<ItemState>, val totalPrice: Double) {
    fun isEmpty() = list.isEmpty()
}