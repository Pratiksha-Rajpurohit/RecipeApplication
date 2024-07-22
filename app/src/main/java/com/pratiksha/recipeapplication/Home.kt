package com.pratiksha.recipeapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.pratiksha.recipeapplication.databinding.FragmentHomeBinding
import org.checkerframework.checker.mustcall.qual.Owning

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {

    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var arrayRecipe : ArrayList<Recipe>
    private lateinit var recipeAdapter : RecipeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container , false)

        recyclerView = binding.recyclerRecipe
        recyclerView.layoutManager = GridLayoutManager(context,1, GridLayoutManager.VERTICAL,false)
//        recycler view smooth
        recyclerView.setHasFixedSize(true)


        showRecipes()
        return binding.root
    }

    fun showRecipes() {
        val db = Firebase.firestore
//      println("Show recipe is called............")

        arrayRecipe = arrayListOf<Recipe>()



        db.collection("recipes").get()
            .addOnSuccessListener {
//              println("Show recipe success.................................")
                for (document in it){
                    val recipeData = document.data

                    try {

                        if(recipeData != null){
//                          println("TRY BLOCK.................................")

                            val recipeName = recipeData["Name"] as String
                            val recipeIngredient = recipeData["Ingredient"] as String
                            val recipeInstruction = recipeData["Instruction"] as String
                            val recipeImage = recipeData["Image"] as String
                            val recipeOwner = recipeData["Owner"] as String
                            val recipeId = document.id
                            // println("DATA MILA>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                            // println("RECIPE ID >>>>>>>>>>>>>>>> $recipeId")

                            val recipe = Recipe(recipeName,recipeIngredient,recipeInstruction,recipeImage,recipeOwner , recipeId  )

                            arrayRecipe.add(recipe)
//                          println("NAME : $arrayRecipe ...................................................")


                        }

                    }catch (e : Exception){
//                      println("EXCEPTION.................................")
                        e.printStackTrace()
                    }

                    if(arrayRecipe.isNotEmpty()){
                        recipeAdapter = RecipeAdapter("home",arrayRecipe,requireContext())

                        recyclerView.adapter = recipeAdapter


                    }else{
                        println("NO RECIPE....................................")
                    }
                }
            }
            .addOnFailureListener{
                println("Fail to get Recipe Data........ $it")
            }
    }

}
