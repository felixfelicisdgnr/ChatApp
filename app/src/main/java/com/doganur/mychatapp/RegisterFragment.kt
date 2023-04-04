package com.doganur.mychatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.doganur.mychatapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseFirestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        auth = FirebaseAuth.getInstance()
        databaseFirestore = FirebaseFirestore.getInstance()

        _binding = FragmentRegisterBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerLoginTextView.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.signUpButton.setOnClickListener {
            //sign up buttona tıklandığında yapılacaklar, kullanıcı kayıt yapacak
            performSignUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performSignUp() {

        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.registerPasswordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        val userHashMap = hashMapOf<String, String>()
        userHashMap["username"] = username
        userHashMap["email"] = email
        userHashMap["password"] = password
        userHashMap["confirmPassword"] = confirmPassword

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the fields for firestore!", Toast.LENGTH_LONG).show()
            return
        } else {
            if (password == confirmPassword) {
                databaseFirestore.collection("UserData").add(userHashMap)
                    .addOnSuccessListener { documentReference ->

                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            findNavController().navigate(action)
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                        Log.d("DOGA", "User added with ID: ${documentReference.id}")
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

        } else {
                Toast.makeText(requireContext(), "Password is not matching!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
