package com.pratiksha.recipeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.ConfirmationCallback
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.pratiksha.recipeapplication.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUpTxt : TextView
    private lateinit var emailEdt : EditText
    private lateinit var passwordEdt : EditText
    private lateinit var confirmPass : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpTxt = findViewById(R.id.sign_up_txt)
        emailEdt = findViewById(R.id.email_edt)
        passwordEdt = findViewById(R.id.password_edt)
        confirmPass = findViewById(R.id.confirm_password_edt)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpTxt.setOnClickListener {
            val intent = Intent(this@SignUpActivity , SignInActivity::class.java )
            startActivity(intent)
        }

        binding.signupBtn.setOnClickListener {
            val email = emailEdt.text.toString()
            val pass = passwordEdt.text.toString()
            val cPass = confirmPass.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && cPass.isNotEmpty() ){

                if(pass == cPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"Registration Done",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,SignInActivity::class.java))
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }else{
                Toast.makeText(this, "fill all empty filled",Toast.LENGTH_SHORT).show()
            }

         }



    }
}