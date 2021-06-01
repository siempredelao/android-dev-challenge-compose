package com.example.androiddevchallenge.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.model.ItemState
import com.example.androiddevchallenge.model.Recipe
import com.example.androiddevchallenge.model.RecipesDataGenerator
import com.example.androiddevchallenge.model.RecipesListViewModel
import com.example.androiddevchallenge.model.State
import com.example.androiddevchallenge.ui.theme.DarkGray
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlin.math.roundToInt

/**
 * Main task screen composable
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipesListScreen(viewModel: RecipesListViewModel = RecipesListViewModel()) {
    val uiStateModel: UiStateModel by viewModel.uiStateModel.observeAsState(
        UiStateModel(
            emptyList(),
            0.0
        )
    )
    Column {
        ColorFilter(onClick = {
            viewModel.onFilterByColor(it)
        })

        if (uiStateModel.isEmpty()) {
            EmptyView(Modifier.weight(1f))
        } else {
            RecipeListView(uiStateModel.list, Modifier.weight(1f),
                onLongClick = { viewModel.onRecipeLongClick(it) },
                onYesClick = { viewModel.onRecipeDelete(it) },
                onNoClick = { viewModel.onRecipeReset(it) }
            )
        }

        BottomView(uiStateModel.totalPrice) { viewModel.onAddRecipeClick() }
    }
}

/**
 * Displays list of recipes
 */
@ExperimentalMaterialApi
@Composable
fun RecipeListView(
    list: List<ItemState>,
    modifier: Modifier,
    onLongClick: (Int) -> Unit,
    onYesClick: (Int) -> Unit,
    onNoClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.background(DarkGray)
    ) {
        items(list) { item ->
            if (item.state == State.RECIPE) {
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it != DismissValue.DismissedToStart) {
                            onLongClick(item.recipe.id)
                        }
                        it == DismissValue.DismissedToEnd
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    background = {},
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    dismissContent = { RecipeCard(item.recipe, onLongClick) }
                )

            } else {
                ConfirmDeletionCard(
                    item.recipe,
                    onYesClick = onYesClick,
                    onNoClick = onNoClick
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}

/**
 * Draws an "Add" button
 */
@Composable
fun AddButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(text = "Add recipe")
    }
}

/**
 * Card which displays a recipe with name, color and price
 */
@ExperimentalMaterialApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeCard(recipe: Recipe, onLongClick: (Int) -> Unit = {}) {
    Card(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = { onLongClick(recipe.id) },
                onClick = {}
            )
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            val centerVerticalAlignment = Modifier.align(Alignment.CenterVertically)
            ColorView(color = recipe.color, centerVerticalAlignment)
            RecipeName(
                recipe,
                centerVerticalAlignment
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            VerticalDivider(centerVerticalAlignment)
            RecipePrice(recipe, centerVerticalAlignment)
        }
    }
}

/**
 * Card which shows a request to remove a particular recipe from the list
 */
@Composable
fun ConfirmDeletionCard(
    recipeToDelete: Recipe,
    onYesClick: (Int) -> Unit = {},
    onNoClick: (Int) -> Unit = {}
) {
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            Modifier
                .background(color = recipeToDelete.color)
                .padding(16.dp),
        ) {
            Text(
                text = "Remove from the list?",
                modifier = Modifier.padding(8.dp)
            )
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val weightModifier = Modifier.weight(1f)
                ConfirmationButton(text = "Yes", modifier = weightModifier) {
                    onYesClick(recipeToDelete.id)
                }
                ConfirmationButton(text = "No", modifier = weightModifier) {
                    onNoClick(recipeToDelete.id)
                }
            }
        }
    }
}

/**
 * Small color indicator for a recipe
 */
@Composable
fun ColorView(color: Color, modifier: Modifier) {
    Spacer(
        modifier = modifier
            .width(8.dp)
            .height(52.dp)
            .background(color, shape = RoundedCornerShape(6.dp))
    )
}

/**
 * Displays a list of color tags available for filtering. Check [BonusComponentsReview]
 *
 * Use this view for Bonus task
 */
@Composable
fun ColorFilter(onClick: (Color) -> Unit) {
    Row(
        Modifier
            .background(DarkGray)
            .padding(vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        RecipesDataGenerator.colors.forEach { color ->
            ColorView3(color = color, onClick = onClick)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorView3(color: Color, modifier: Modifier = Modifier, onClick: (Color) -> Unit) {
    Spacer(
        modifier = modifier
            .width(64.dp)
            .height(24.dp)
            .background(color, shape = RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = { onClick(color) }
            )
    )
}

@Preview
@Composable
fun BonusComponentsReview() {
    MyTheme {
        ColorFilter(onClick = {})
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ComponentsPreview() {
    val recipe = RecipesDataGenerator.generateRecipes(1).first()
    MyTheme {
        Surface {
            Column {
                RecipeCard(recipe)
                Spacer(modifier = Modifier.size(8.dp))
                ConfirmDeletionCard(recipe, onYesClick = {}, onNoClick = {})
            }
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    MyTheme {
        RecipesListScreen()
    }
}