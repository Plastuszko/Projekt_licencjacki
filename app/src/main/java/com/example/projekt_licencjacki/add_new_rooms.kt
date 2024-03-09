package com.example.projekt_licencjacki

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_licencjacki.databinding.AddNewRoomsBinding



private lateinit var binding: AddNewRoomsBinding
var Hours: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
var Map_to_add: MutableMap<MutableMap<String, MutableMap<String, Any>>, Any> = mutableMapOf()


class add_new_rooms: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNewRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkboxHour1.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour1.isChecked){
                    binding.checkboxAll.isChecked=false

            }
        }
        binding.checkboxHour2.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour2.isChecked){
                    binding.checkboxAll.isChecked=false

            }
        }
        binding.checkboxHour3.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour3.isChecked){
                    binding.checkboxAll.isChecked=false

            }
        }
        binding.checkboxHour4.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour4.isChecked){
                    binding.checkboxAll.isChecked=false
                }

        }
        binding.checkboxHour5.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour5.isChecked){
                    binding.checkboxAll.isChecked=false
                }

        }
        binding.checkboxHour6.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour6.isChecked){
                    binding.checkboxAll.isChecked=false
                }

        }
        binding.checkboxHour7.setOnCheckedChangeListener{ buttonView, isChecked ->

                if(binding.checkboxAll.isChecked&& !binding.checkboxHour7.isChecked){
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

        }
    }
}