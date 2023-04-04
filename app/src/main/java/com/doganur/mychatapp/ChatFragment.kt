package com.doganur.mychatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.doganur.mychatapp.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseFirestore : FirebaseFirestore
    private lateinit var adapter: ChatRecyclerAdapter
    private var chatsList = arrayListOf<ChatData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        databaseFirestore = FirebaseFirestore.getInstance()

        _binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatRecyclerAdapter()
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendMessage(){

        auth.currentUser?.let { it ->

            databaseFirestore.collection("UserData").document("username").get().addOnSuccessListener {

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }

            val user = it.email
            val chatText = binding.chatEditText.text.toString()
            val date = FieldValue.serverTimestamp() //güncel zamanu alıyor

            val dataMap = HashMap<String, Any>()
            dataMap["text"] = chatText
            dataMap["user"] = user!!
            dataMap["date"] = date

            databaseFirestore.collection("Chats").add(dataMap).addOnSuccessListener {
                binding.chatEditText.setText("")
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                binding.chatEditText.setText("")
            }


        }

        databaseFirestore.collection("Chats").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->

            if(error != null ){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (value.isEmpty) {
                        Toast.makeText(requireContext(),"Haven't got a message", Toast.LENGTH_LONG).show()
                    } else {
                        val documents = value.documents

                        chatsList.clear()

                        for (document in documents) {
                            val text = document.get("text") as String
                            val user = document.get("user") as String
                            val chat = ChatData(user, text)
                            chatsList.add(chat)
                            adapter.chats = chatsList
                        }

                    }

                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

}