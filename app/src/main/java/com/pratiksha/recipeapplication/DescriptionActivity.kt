package com.pratiksha.recipeapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.pratiksha.recipeapplication.databinding.ActivityDescriptionBinding
import com.pratiksha.recipeapplication.databinding.ActivityMainBinding


class DescriptionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDescriptionBinding
    private lateinit var nameOfRecipe : TextView
    private lateinit var ingredientOfRecipe : TextView
    private lateinit var instructionOfRecipe : TextView
    private lateinit var imageView: ImageView
//    lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_description)

        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameOfRecipe = binding.recipeNameTxtV
        ingredientOfRecipe = binding.ingredientTxtV
        instructionOfRecipe = binding.instructionTxtV
        imageView = binding.recipeImgV

//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = "Book Details"

        if(intent != null){
            val recipeId = intent.getStringExtra("recipeId")
            println("INTEND Not NULL")
            recipes(recipeId)
        }else{
            println("INTEND NULL")
        }


    }

    fun recipes(recipeId : String? ){

        println("recipes open")

        val db = Firebase.firestore



//        val recipeName = "DBsqd2NzoEkfKKkTPpAz"

        db.collection("recipes")
            .document(recipeId!!)
            .get()
            .addOnSuccessListener {
               
                println("$it")

                nameOfRecipe.text = it["Name"].toString()
                ingredientOfRecipe.text = it["Ingredient"].toString()
                instructionOfRecipe.text = it["Instruction"].toString()

                loadImageFromeUrl(it["Image"].toString(),imageView)




            }
            .addOnFailureListener{
                println("exception :  $it ")

            }
    }

    private fun loadImageFromeUrl(downloadImageUri: String, image: ImageView) {

        if( downloadImageUri != null) {

            Glide.with(this).load(downloadImageUri).placeholder(R.drawable.add_image)
                .error(R.drawable.add_image).into(image)

        }

    }
}
