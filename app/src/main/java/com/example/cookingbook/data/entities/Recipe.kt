package com.example.cookingbook.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class RecipeStep(
    var recipeId: Int, val content: String, val order: Int,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
)

@Entity
data class RecipeIngredient(
    var recipeId: Int,
    val name: String,
    val amount: Double,
    val unit: String,
    val comment: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
)

@Entity
data class Recipe(
    val name: String,
    val difficulty: String,
    val cookingTime: Int,
    val servingSize: Int,
    val calories: Int,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
) {
    override fun toString(): String {
        return "%s | %s | %d kcal".format(name, difficulty, calories)
    }
}

data class RecipeWithStepsAndIngredients(
    @Embedded val recipe: Recipe, @Relation(
        entity = RecipeStep::class, parentColumn = "uid", entityColumn = "recipeId"
    ) val steps: List<RecipeStep>, @Relation(
        entity = RecipeIngredient::class, parentColumn = "uid", entityColumn = "recipeId"
    ) val ingredients: List<RecipeIngredient>
)