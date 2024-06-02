package com.example.projekt_licencjacki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projekt_licencjacki.databinding.LoginScreenBinding
import com.google.firebase.auth.FirebaseAuth


const val EXTRA_MESSAGE="com.example.Projekt_licencjacki.MESSAGE"

class login_screen : AppCompatActivity() {
private lateinit var binding: LoginScreenBinding
private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.loginbutton.setOnClickListener{
            val email = binding.loginText.text.toString()
            val password = binding.passwordText.text.toString()

            if(email.isNotEmpty()&&password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        val intent= Intent(this,list_of_rooms::class.java)
                        intent.putExtra("USER",email)
                        startActivity(intent)
                        finish()
                    }else(
                        Toast.makeText(this,"Wrong login or password",Toast.LENGTH_SHORT).show()
                    )
                }
            }else{
                Toast.makeText(this,"Please input data",Toast.LENGTH_SHORT).show()
            }

        }

    }


}




