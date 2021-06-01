package com.example.androiddevchallenge.model

import androidx.compose.ui.graphics.Color

class RecipeRepository {
    private val list = mutableListOf<Recipe>()

    fun filterByColor(color: Color): List<Recipe> {
        return list.filter { it.color == color }
    }

    fun saveRecipe(recipe: Recipe) {
        list.add(recipe)
    }

    fun deleteRecipe(recipeId: Int) {
        list.removeIf { it.id == recipeId }
    }
}