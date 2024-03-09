package com.example.projekt_licencjacki

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_licencjacki.databinding.AddNewRoomsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.UUID
import kotlin.random.Random


private lateinit var binding: AddNewRoomsBinding
var roomId=ArrayList<String>()
var Hours: MutableMap<String, Any> = mutableMapOf()
var Map_to_add: MutableMap<String, Any> = mutableMapOf()
var db = Firebase.firestore

class add_new_rooms: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNewRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkboxHour1.setOnCheckedChangeListener{ buttonView, isChecked ->
                if(binding.checkboxHour1.isChecked){
                    Hours[binding.checkboxHour1.text.toString()]= mutableMapOf(
                        "booked" to false,
                        "who" to ""
                    )
                }else if(!binding.checkboxHour1.isChecked){
                    Hours.remove(binding.checkboxHour1.text.toString())
                }
                if(binding.checkboxAll.isChecked && !binding.checkboxHour1.isChecked){
                    binding.checkboxAll.isChecked=false
                }
        }
        binding.checkboxHour2.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.checkboxHour2.isChecked){
                Hours[binding.checkboxHour2.text.toString()]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )

            }else if(!binding.checkboxHour2.isChecked){
                Hours.remove(binding.checkboxHour2.text.toString())
            }
                if(binding.checkboxAll.isChecked && !binding.checkboxHour2.isChecked){
                    binding.checkboxAll.isChecked=false

            }
        }
        binding.checkboxHour3.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.checkboxHour3.isChecked){
                Hours[binding.checkboxHour3.text.toString()]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )

            }else if(!binding.checkboxHour3.isChecked){
                Hours.remove(binding.checkboxHour3.text.toString())
            }
            if(binding.checkboxAll.isChecked && !binding.checkboxHour3.isChecked){
                binding.checkboxAll.isChecked=false
            }
        }
        binding.checkboxHour4.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.checkboxHour4.isChecked){
                Hours[binding.checkboxHour4.text.toString()]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )

            }else if(!binding.checkboxHour4.isChecked){
                Hours.remove(binding.checkboxHour4.text.toString())
            }
            if(binding.checkboxAll.isChecked && !binding.checkboxHour4.isChecked){
                binding.checkboxAll.isChecked=false
            }
        }
        binding.checkboxHour5.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.checkboxHour5.isChecked){
                Hours[binding.checkboxHour5.text.toString()]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )

            }else if(!binding.checkboxHour5.isChecked){
                Hours.remove(binding.checkboxHour5.text.toString())
            }
            if(binding.checkboxAll.isChecked && !binding.checkboxHour5.isChecked){
                binding.checkboxAll.isChecked=false
            }
        }
        binding.checkboxHour6.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.checkboxHour6.isChecked){
                Hours[binding.checkboxHour6.text.toString()]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )

            }else if(!binding.checkboxHour6.isChecked){
                Hours.remove(binding.checkboxHour6.text.toString())
            }
            if(binding.checkboxAll.isChecked && !binding.checkboxHour6.isChecked){
                binding.checkboxAll.isChecked=false
            }
        }
        binding.checkboxHour7.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.checkboxHour7.isChecked){
                Hours[binding.checkboxHour7.text.toString()]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )

            }else if(!binding.checkboxHour7.isChecked){
                Hours.remove(binding.checkboxHour7.text.toString())
            }
            if(binding.checkboxAll.isChecked && !binding.checkboxHour7.isChecked){
                binding.checkboxAll.isChecked=false
            }
        }


        binding.checkboxAll.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                binding.checkboxAll.isChecked=true
                binding.checkboxHour1.isChecked = true
                binding.checkboxHour2.isChecked = true
                binding.checkboxHour3.isChecked = true
                binding.checkboxHour4.isChecked = true
                binding.checkboxHour5.isChecked = true
                binding.checkboxHour6.isChecked = true
                binding.checkboxHour7.isChecked = true
            }

        }
        binding.addResetBtn.setOnClickListener{
            checkRooms()
            for (i in roomId.indices) {
                Map_to_add[roomId[i]] = mutableListOf(Hours)
            }

            if(Map_to_add.isEmpty()|| Hours.isEmpty()){
                Toast.makeText(this,"Please choose data to add",Toast.LENGTH_SHORT).show()
            }else {
                Log.d(TAG, "map to add: " + Map_to_add.toString())
            }
        }
    }


    private fun addRooms(){


        for(i in roomId.indices) {
            db.collection("rooms").document(roomId[i]).collection("Days").document()
//                .set()

        }

    }
     fun checkRooms(){
         Log.d(TAG,generateRandomKey())
//        db.collection("rooms")
//            .get()
//            .addOnSuccessListener { roomIds ->
//                for(document in roomIds){
//                    roomId.add(document.get("rid").toString())
//                }
//            }
    }
    fun generateRandomKey(): String {
        val alphanumericChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..20)
            .map { alphanumericChars[Random.nextInt(0, alphanumericChars.length)] }
            .joinToString("")
    }

}