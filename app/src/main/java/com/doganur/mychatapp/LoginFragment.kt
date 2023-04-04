package com.doganur.mychatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.doganur.mychatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        databaseFirestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
            findNavController().navigate(action)
        }

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginSignUpTextView.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.loginLoginButton.setOnClickListener {
            loginFunc()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            forgotPassword()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginFunc() {

        val username = binding.loginUsernameEditText.text.toString()
        val password = binding.loginPasswordEditText.text.toString()


        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill al the fields!", Toast.LENGTH_SHORT)
                .show()
            return
        } else {

            databaseFirestore.collection("UserData").whereEqualTo("username", username).get()
                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {

                        val email = documents.documents[0].get("email").toString()

                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                            val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                            findNavController().navigate(action)
                        }
                            .addOnFailureListener { exception ->
                                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()

            }

        }
    }

    private fun forgotPassword(){

    }

}

