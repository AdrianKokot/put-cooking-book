package com.example.cookingbook.models

data class Recipe(
    val name: String,
    val description: String,
    val difficulty: String,
    val ingredients: List<String>,
    val cookingSteps: List<String>
)

object RecipeList {
    val recipes = listOf(
        Recipe(
            "Spaghetti with Meat Sauce",
            "A classic Italian dish that's easy to make and delicious to eat.",
            "Easy",
            listOf(
                "1 pound spaghetti",
                "1 pound ground beef",
                "1 onion, chopped",
                "2 cloves garlic, minced",
                "1 can (28 ounces) crushed tomatoes",
                "1 teaspoon dried basil",
                "1 teaspoon dried oregano",
                "Salt and pepper to taste"
            ),
            listOf(
                "Cook the spaghetti according to the package directions.",
                "Meanwhile, in a large skillet, cook the ground beef over medium heat until browned.",
                "Add the onion and garlic and cook until the onion is softened.",
                "Stir in the crushed tomatoes, basil, oregano, salt, and pepper.",
                "Bring to a simmer and cook for 10-15 minutes.",
                "Serve the meat sauce over the cooked spaghetti."
            )
        ),
        Recipe(
            "Chicken Parmesan",
            "A classic Italian-American dish that's hearty and delicious.",
            "Moderate",
            listOf(
                "4 boneless, skinless chicken breasts",
                "1/2 cup all-purpose flour",
                "2 eggs, beaten",
                "1 cup Italian breadcrumbs",
                "1/2 cup grated Parmesan cheese",
                "1/4 cup olive oil",
                "1 jar (24 ounces) marinara sauce",
                "1 cup shredded mozzarella cheese"
            ),
            listOf(
                "Preheat the oven to 375°F.",
                "Pound the chicken breasts until they are even in thickness.",
                "Season the chicken with salt and pepper.",
                "Dredge the chicken in the flour, shaking off any excess.",
                "Dip the chicken in the beaten eggs, then coat in the breadcrumb mixture.",
                "Heat the olive oil in a large skillet over medium heat.",
                "Add the chicken and cook until browned on both sides.",
                "Place the chicken in a baking dish and cover with marinara sauce.",
                "Sprinkle the shredded mozzarella cheese over the top.",
                "Bake for 25-30 minutes, or until the chicken is cooked through and the cheese is melted and bubbly."
            )
        )
    )
}
