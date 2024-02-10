package com.pratiksha.recipeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.pratiksha.recipeapplication.databinding.ActivitySignInBinding




class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailEdt : EditText
    private lateinit var passEdt : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEdt = findViewById(R.id.email_edt)
        passEdt = findViewById(R.id.password_edt)

//        initialize firebase
        firebaseAuth = FirebaseAuth.getInstance()


        binding.signInTxt.setOnClickListener {

            val intent = Intent(this@SignInActivity , SignUpActivity::class.java)
            startActivity(intent)

        }

        binding.signinBtn.setOnClickListener {
            val email = emailEdt.text.toString()
            val pass = passEdt.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if(it.isSuccessful){

                        Toast.makeText(this@SignInActivity,"signIn Successful",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))

                    }else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }





    }

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            Toast.makeText(this,"Signed in as :${currentUser.email}", Toast.LENGTH_SHORT).show()

            startActivity(
                Intent(this, MainActivity::class.java)
            )

        }else{
            Toast.makeText(this, "no user signIn",Toast.LENGTH_SHORT).show()
        }
    }
}