package com.example.androiddevchallenge.model

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.components.UiStateModel

class RecipesListViewModel : ViewModel() {

    private val repository = RecipeRepository()

    private val _uiModel = MutableLiveData(UiStateModel(emptyList(), 0.0))
    val uiStateModel: LiveData<UiStateModel> = _uiModel

    fun onAddRecipeClick() {
        _uiModel.value?.let {
            val recipe = RecipesDataGenerator.generateRecipes(1).first()
            repository.saveRecipe(recipe)
            val list = it.list + ItemState(recipe, State.RECIPE)
            val totalPrice = calculatePrice(list)
            _uiModel.value = UiStateModel(list = list, totalPrice = totalPrice)
        }
    }

    fun onRecipeLongClick(recipeId: Int) { // Changes the state and updates the list
        _uiModel.value?.let {
            val updatedList = it.list.map { itemState ->
                if (itemState.recipe.id == recipeId) {
                    itemState.copy(state = State.CONFIRMATION)
                } else {
                    itemState
                }
            }
            _uiModel.value = it.copy(list = updatedList)
        }
    }

    fun onRecipeDelete(recipeId: Int) { // Deletes the item from the list
        _uiModel.value?.let {
            repository.deleteRecipe(recipeId)
            val mutableList = it.list.toMutableList()
            val finalList = mutableList.filter { item -> item.recipe.id != recipeId }
            val totalPrice = calculatePrice(finalList)
            _uiModel.value = UiStateModel(list = finalList, totalPrice = totalPrice)
        }
    }

    fun onRecipeReset(recipeId: Int) { // Resets the item to the "recipe" state
        _uiModel.value?.let {
            val updatedList = it.list.map { itemState ->
                if (itemState.recipe.id == recipeId) {
                    itemState.copy(state = State.RECIPE)
                } else {
                    itemState
                }
            }
            _uiModel.value = it.copy(list = updatedList)
        }
    }

    fun onFilterByColor(color: Color) {
        val filteredList = repository.filterByColor(color)
        _uiModel.value?.let {
            _uiModel.value = it.copy(list = filteredList.map { recipe -> ItemState(recipe, State.RECIPE) }) // TODO we need to keep the state from the original list
        }
    }

    private fun calculatePrice(list: List<ItemState>) = list.sumOf { it.recipe.price } / 100
}