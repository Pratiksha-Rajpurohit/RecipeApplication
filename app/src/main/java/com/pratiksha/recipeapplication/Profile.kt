package com.pratiksha.recipeapplication

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.pratiksha.recipeapplication.databinding.FragmentProfileBinding
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {

    private lateinit var _binding : FragmentProfileBinding
    private val binding get() = _binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recipeAdapter : RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayRecipe : ArrayList<Recipe>
//    private lateinit var profileImage : CircleImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        init()
        recyclerView = binding.recyclerRecipe
//        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)

        val db = Firebase.firestore

        val currentUser = firebaseAuth.currentUser




        db.collection("User")
            .document(currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->

                if(document != null){
                    arrayRecipe = arrayListOf<Recipe>()

//                    println("8888888888888 $document")
//                    println("8888888888888 ${document["recipes"]}")

                    for(i in document["recipes"] as List<String>) {

                        db.collection("recipes")
                            .document(i)
                            .get()
                            .addOnSuccessListener { document ->


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
                                    recipeAdapter = RecipeAdapter("profile",arrayRecipe,requireContext())

                                    recyclerView.adapter = recipeAdapter


                                }else{
//                                    println("NO RECIPE....................................")
                                }

                            }


                    }


                }

            }

// profile image click listener
        binding.userImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent , 101)
        }



//  signOut logic
        binding.sigOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(
                Intent(activity, SignInActivity::class.java)
            )
            Toast.makeText(activity , "logged out successfully",Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 101){
            binding.userImage.setImageURI(data?.data)
            println("IMAGE ADDED $data")
            uploadProfileImage(data?.data)
        }
    }

    private fun uploadProfileImage(uri : Uri?) {

        val profileImageName = UUID.randomUUID().toString()+".jpg"
        val storageReference = FirebaseStorage.getInstance().getReference().child("profile_Images/$profileImageName")



        storageReference.putFile(uri!!).addOnSuccessListener {

            val result = it.metadata?.reference?.downloadUrl

            val current = firebaseAuth.currentUser?.uid.toString()

            Log.i("mili" , "user $current")

            result?.addOnSuccessListener {
                Log.i("mili" , "uri:------> $it ")
                Firebase.firestore.collection("User").document(firebaseAuth.currentUser?.uid.toString()).update("userProfileImage",it.toString())
                    .addOnSuccessListener {
                        Log.i("mili" , "success $it")
                    }
                    .addOnFailureListener{
                        Log.i("mili" , "failure $it")
                    }

            }
        }
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()

        Firebase.firestore.collection("User").document(Firebase.auth.uid!!.toString()).get().addOnSuccessListener {
            if(it.exists()){
                val profileLink = it["userProfileImage"].toString()

//                Log.i("hua" , it.toString())

                if(!profileLink.isNullOrBlank()){
                    Glide.with(this)
                        .load(profileLink)
                        .into(binding.userImage)

                }else{

//                    println("OOOOOOOOOOOOOppppppppppppppppppppppppssssssssssssss it's not working :(")

                    binding.userImage.setImageResource(R.drawable.user2)
                }

            }

        }
    }



}