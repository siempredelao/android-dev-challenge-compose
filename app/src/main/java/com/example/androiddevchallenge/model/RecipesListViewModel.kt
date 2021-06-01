package com.example.androiddevchallenge.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.components.UiModel

class RecipesListViewModel : ViewModel() {

    private val _uiModel = MutableLiveData(UiModel(emptyList(), 0.0))
    val uiModel: LiveData<UiModel> = _uiModel

    fun onAddRecipeClick() {
        _uiModel.value?.let {
            val list = it.list + RecipesDataGenerator.generateRecipes(1)
            val totalPrice = calculatePrice(list)
            _uiModel.value = UiModel(list = list, totalPrice = totalPrice)
        }
    }

    fun onRecipeLongClick() { // Changes the state and updates the list

    }

    fun onRecipeDelete(recipeId: Int) { // Deletes the item from the list
        _uiModel.value?.let {
            val mutableList = it.list.toMutableList()
            val finalList = mutableList.filter { item -> item.id != recipeId }
            val totalPrice = calculatePrice(finalList)
            _uiModel.value = UiModel(list = finalList, totalPrice = totalPrice)
        }
    }

    fun onRecipeReset(recipeId: Int) { // Resets the item to the "recipe" state

    }

    private fun calculatePrice(list: List<Recipe>) = list.sumOf { it.price } / 100
}