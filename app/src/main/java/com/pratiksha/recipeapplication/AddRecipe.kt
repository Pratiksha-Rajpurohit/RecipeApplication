package com.pratiksha.recipeapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.TotpMultiFactorAssertion
import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pratiksha.recipeapplication.databinding.FragmentAddRecipeBinding
import com.pratiksha.recipeapplication.databinding.FragmentProfileBinding
import java.lang.Math.log
import java.util.UUID
import com.pratiksha.recipeapplication.User



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddRecipe.newInstance] factory method to
 * create an instance of this fragment.
 */

class AddRecipe : Fragment() {

    private lateinit var _binding: FragmentAddRecipeBinding
    private val binding get() = _binding
    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var buttonSelectImage: Button
    private var selectImageUri : Uri? = null
    private var downloadImageUri: String? = null
    private lateinit var imageViewRecipe : ImageView
    private lateinit var saveRecipeButton : Button
    private lateinit var  progressDialog : ProgressDialog

//    private lateinit var instruction :String
//    private lateinit var ingredient : String
//    private lateinit var recipeName : String

    companion object{
        private const val  PICK_IMAGE_REQUEST = 1
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddRecipeBinding.inflate(inflater,container,false)

//       initialize firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore


//        select image
//        buttonSelectImage = binding.btnSelectImage
        imageViewRecipe = binding.imageView
        saveRecipeButton = binding.saveRecipeBtn

//        buttonSelectImage.setOnClickListener {
//            openGallery()
//        }

        imageViewRecipe.setOnClickListener {
            openGallery()



        }

//        data store in storage and database

        var instruction = binding.instructionEdt.text
        val ingredient = binding.ingredientEdt.text
        val recipeName = binding.recipeNameEdt.text




        saveRecipeButton.setOnClickListener {
//            println("HELLLLLOOOOOOOO IDHR DHYAN DOOOOOOO...................................... ")
//            println("instruction = $ins ")

            if (selectImageUri != null  && instruction!!.isNotEmpty() && ingredient!!.isNotEmpty() && recipeName!!.isNotEmpty()) {
                uploadImageToCloudStorage()

            }
            else{
                Toast.makeText(context, "Please Select an Image and add all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private  fun saveToDatabase(){
        val msg = "saving Recipe..."
        val title = "Save Recipe..."
        createProgressDialog(msg , title)

        val instruction = binding.instructionEdt.text.toString()
        val ingredient = binding.ingredientEdt.text.toString()
        val recipeName = binding.recipeNameEdt.text.toString()
        val userId = firebaseAuth.currentUser?.email

        val recipe = hashMapOf(
            "Name" to recipeName,
            "Ingredient" to ingredient,
            "Instruction" to instruction,
            "Image" to downloadImageUri,
            "Owner" to userId
        )

        Firebase.firestore.collection("recipes")
            .add(recipe)
            .addOnSuccessListener {it: DocumentReference ->
                dismissProgressDialog()
                Toast.makeText(context,  "Recipe Added Successfully",Toast.LENGTH_SHORT).show()

                println(it) // it print  DocumentReference
                saveRecipeIdToProfile(it.id.toString())// " it " refer to "DocumentReference"




            }
            .addOnFailureListener{it:Exception ->
                dismissProgressDialog()
                Toast.makeText(context , "Fail to Upload Image.. ${it}", Toast.LENGTH_SHORT).show()
                println("Recipe save fail: ${it}")


            }

    }

    private fun saveRecipeIdToProfile(recipeId : String){
        val userId = firebaseAuth.currentUser?.uid
        val email = firebaseAuth.currentUser?.email
        println("saveRecipe to profile add with ID : $recipeId")

        if(userId != null && email != null){

           val db =  Firebase.firestore
            val userCollection = db.collection("users")

            userCollection.document(userId).get()
                .addOnSuccessListener {
//                    condition check tha document of userId exist or not
                    if (it.exists()) {
                        print("tha document of userId exist")
//                        user document exist update the recipe array

                        val user = it.toObject<User>(User::class.java)
                        user?.let {
                            it.recipes.add(recipeId)
                            userCollection.document(userId).set(user)
                                .addOnSuccessListener {
                                    println("Recipe ID added to user document")
                                }
                                .addOnFailureListener {
                                    println("Failed to update user document: $it")
                                }
                       }

                    }else {
                            print("tha document of userId doesn't exist")

                            val newUser = User(userId, email, mutableListOf(recipeId))
                            db.collection("User").document(userId).set(newUser)
                                .addOnSuccessListener {
                                    println("new User Document Created with the recipe ID....")
                                }
                                .addOnFailureListener {
                                    println("fail to create New user Document $it....")
                                }
                        }
                    }
                .addOnFailureListener{
                    println("fail to get user Document $it....")
                }
                }
        else{
            println("User is not authenticated")
        }



    }

    private fun createProgressDialog( message: String? , title: String?){
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    private fun dismissProgressDialog(){

        progressDialog.dismiss()
    }


    private fun uploadImageToCloudStorage(){

        val msg = "uploading your image..."
        val title = "Uploading..."
        createProgressDialog(msg,title)

//        Storage location
        val ref : StorageReference  = FirebaseStorage.getInstance().getReference()
            .child(UUID.randomUUID().toString())



//      put image in that storage
        ref.putFile(selectImageUri!!)
            .addOnSuccessListener {  taskSnapshot ->
                dismissProgressDialog()

                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { it : Uri ->
                        downloadImageUri = it.toString()
                        println("image Store Successful: $downloadImageUri")
                        saveToDatabase()




                    }

            }
            .addOnFailureListener{it:Exception ->
                downloadImageUri = null
                dismissProgressDialog()
                Toast.makeText(context , "Fail to Upload Image.. ${it}", Toast.LENGTH_SHORT).show()
                println("Image upload failed: ${it}")
            }

    }


    @SuppressLint("SuspiciousIndentation")
    private fun openGallery(){
      val intent = Intent(Intent.ACTION_PICK)
      intent.type = "image/*"

        startActivityForResult(intent, PICK_IMAGE_REQUEST)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            selectImageUri = data.data
            println(selectImageUri)
            if(selectImageUri != null){
                imageViewRecipe.setImageURI(selectImageUri)
            }

        }
    }


}