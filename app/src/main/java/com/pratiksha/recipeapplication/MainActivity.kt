package com.pratiksha.recipeapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pratiksha.recipeapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
//    private lateinit var bottomNavigationView :BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        replaceFragment(Home())

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.addRecipe -> replaceFragment(AddRecipe())
                R.id.profile -> replaceFragment(Profile())

                else -> {

                }


            }
            true
        }



    }

     @SuppressLint("SuspiciousIndentation")
     private fun replaceFragment(fragment : Fragment){
      val fragmentManager =   supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
    }


}