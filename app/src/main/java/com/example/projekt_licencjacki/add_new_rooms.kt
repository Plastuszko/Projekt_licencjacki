package com.example.projekt_licencjacki

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_licencjacki.databinding.AddNewRoomsBinding



private lateinit var binding: AddNewRoomsBinding
class add_new_rooms: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNewRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkboxHour1.setOnClickListener(

        )
    }
}