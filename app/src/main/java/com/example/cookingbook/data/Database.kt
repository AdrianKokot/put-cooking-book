package com.example.cookingbook.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Entity
data class RecipeStep(
    val recipeId: Int, val content: String, val order: Int,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
)

@Entity
data class RecipeIngredient(
    val recipeId: Int,
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
        return "%s (%s) | %d kcal | %d".format(name, difficulty, calories, uid)
    }
}

data class RecipeWithStepsAndIngredients(
    @Embedded val recipe: Recipe, @Relation(
        entity = RecipeStep::class, parentColumn = "uid", entityColumn = "recipeId"
    ) val steps: List<RecipeStep>, @Relation(
        entity = RecipeIngredient::class, parentColumn = "uid", entityColumn = "recipeId"
    ) val ingredients: List<RecipeIngredient>
)

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Transaction
    @Query("SELECT * FROM recipe WHERE uid = :recipeId")
    fun getRecipe(recipeId: Int): RecipeWithStepsAndIngredients

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe, steps: List<RecipeStep>, ingredients: List<RecipeIngredient>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(steps: List<RecipeStep>, ingredients: List<RecipeIngredient>)


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe): Long
}

@Database(entities = [Recipe::class, RecipeStep::class, RecipeIngredient::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        private fun seedDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Executors.newSingleThreadExecutor().execute {
                        val recipeDao = getDatabase(context).recipeDao()

                        var recipe = Recipe(
                            "Pancakes", "Easy", 30, 4, 300
                        )
                        var id = recipeDao.insert(recipe).toInt()

                        var ingredients = listOf(
                            RecipeIngredient(id, "Flour", 1.0, "cup", ""),
                            RecipeIngredient(id, "Milk", 1.0, "cup", ""),
                            RecipeIngredient(id, "Eggs", 2.0, "", ""),
                            RecipeIngredient(id, "Sugar", 1.0, "tbsp", ""),
                            RecipeIngredient(id, "Salt", 1.0, "tsp", ""),
                            RecipeIngredient(id, "Baking powder", 1.0, "tsp", ""),
                            RecipeIngredient(id, "Butter", 1.0, "tbsp", "")
                        )

                        var steps = listOf(
                            RecipeStep(id, "Mix all ingredients", 0),
                            RecipeStep(id, "Cook on a pan", 1)
                        )

                        recipeDao.insert(steps, ingredients)

                        val recipe2 = Recipe(
                            "Pasta", "Easy", 30, 4, 300
                        )
                        val id2 = recipeDao.insert(recipe2).toInt()
                        val ingredients2 = listOf(
                            RecipeIngredient(id2, "Pasta", 1.0, "cup", ""),
                            RecipeIngredient(id2, "Tomato sauce", 1.0, "cup", ""),
                            RecipeIngredient(id2, "Cheese", 1.0, "cup", ""),
                            RecipeIngredient(id2, "Salt", 1.0, "tsp", ""),
                            RecipeIngredient(id2, "Pepper", 1.0, "tsp", "")
                        )

                        val steps2 = listOf(
                            RecipeStep(id2, "Cook pasta", 0),
                            RecipeStep(id2, "Add tomato sauce", 1),
                            RecipeStep(id2, "Add cheese", 2)
                        )

                        recipeDao.insert(steps2, ingredients2)
                    }
                }
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "cooking-book-database.db"
                ).allowMainThreadQueries().addCallback(seedDatabaseCallback(context)).build()
                INSTANCE = instance

                instance
            }
        }
    }
}