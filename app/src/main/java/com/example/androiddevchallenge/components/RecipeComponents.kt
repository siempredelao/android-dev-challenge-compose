package com.example.androiddevchallenge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.model.Recipe
import com.example.androiddevchallenge.ui.theme.DarkGray
import com.example.androiddevchallenge.ui.theme.MyTheme

/**
 * This file is supposed to remain untouched during the challenge. (Unless I overlooked smth)
 */

@Composable
fun EmptyView(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No recipes added",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun RecipeName(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Text(
        text = recipe.name,
        modifier.padding(start = 8.dp, end = 8.dp)
    )
}

@Composable
fun RecipePrice(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Text(
        text = String.format("$ %.2f", recipe.price / 100),
        modifier = modifier
            .padding(start = 8.dp)
            .width(52.dp),
        style = MaterialTheme.typography.subtitle2.copy(fontStyle = FontStyle.Italic)
    )
}

/**
 * Static box with Price + Button
 */
@Composable
fun BottomView() {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Total price",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.onSurface
            )
            Text(text = "$ X", color = MaterialTheme.colors.onSurface)
        }
        AddButton()
    }

}

@Composable
fun VerticalDivider(modifier: Modifier) {
    Spacer(
        modifier = modifier
            .width(1.dp)
            .height(42.dp)
            .background(Color.DarkGray)
    )
}

@Preview
@Composable
fun ComponentsPreview() {
    val recipe = Recipe(name = "My Recipe", price = 12.99, color = Color.Red)
    MyTheme {
        Surface {
            Column {
                EmptyView(modifier = Modifier)
                RecipePrice(recipe = recipe)
                RecipeName(recipe = recipe)
                VerticalDivider(modifier = Modifier.padding(16.dp))
                BottomView()
            }
        }
    }
}