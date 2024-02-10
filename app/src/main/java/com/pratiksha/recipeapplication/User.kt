package com.pratiksha.recipeapplication

data class User(
    var userId: String , // User's ID
    var userEmail: String , // User's email
    var recipes: MutableList<String>  // Array to store recipe IDs
)
