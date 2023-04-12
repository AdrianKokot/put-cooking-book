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
    val category: RecipeCategory,
    val imageId: Int,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
) {
    override fun toString(): String {
        return "%s | %s | %d kcal".format(name, difficulty, calories)
    }
}

data class RecipeWithStepsAndIngredients(
    @Embedded
    val recipe: Recipe,
    @Relation(
        entity = RecipeStep::class, parentColumn = "uid", entityColumn = "recipeId"
    )
    val steps: List<RecipeStep>,
    @Relation(
        entity = RecipeIngredient::class, parentColumn = "uid", entityColumn = "recipeId"
    )
    val ingredients: List<RecipeIngredient>

) {
    fun getIngredientsString(servingSize: Int = recipe.servingSize): String {
        return ingredients.mapIndexed { index, ingredient ->
            "%d. %s | %.2f %s\n\t%s".format(
                index + 1,
                ingredient.name,
                ingredient.amount * servingSize / recipe.servingSize,
                ingredient.unit,
                ingredient.comment
            )
        }.joinToString("\n")
    }

    fun getStepsString(): String {
        return steps.mapIndexed { index, step -> "%d. %s".format((index + 1), step.content) }
            .joinToString("\n\n")
    }
}