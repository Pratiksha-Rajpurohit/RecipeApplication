package com.pratiksha.recipeapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.pratiksha.recipeapplication.databinding.FragmentProfileBinding

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        firebaseAuth = FirebaseAuth.getInstance()


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


}