package com.example.projekt_licencjacki

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projekt_licencjacki.databinding.AddNewRoomsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID
import kotlin.random.Random


private lateinit var binding: AddNewRoomsBinding
private val formatter = SimpleDateFormat("dd.MM.yyyy")
var chosen_date: String=""
val calendar= Calendar.getInstance()
var roomId=ArrayList<String>()
var Hours: MutableMap<String, Any> = mutableMapOf()
var Map_to_add: MutableMap<String, Any> = mutableMapOf()
var db = Firebase.firestore

class add_new_rooms: AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNewRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chooseDateButton.setOnClickListener{
            Log.d(TAG,"before:$ $chosen_date")
            DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(
                Calendar.DAY_OF_MONTH)).show()
            Log.d(TAG,"after : $chosen_date")
        }
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
        binding.addResetBtn.setOnClickListener {
            checkRooms()
            if (chosen_date.isNotEmpty()) {

                for (i in roomId.indices) {
                    Map_to_add[roomId[i]] = Hours.toMutableMap()
                }

                if (Map_to_add.isEmpty() || Hours.isEmpty()) {
                    Toast.makeText(this, "Please choose data to add", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "map to add: " + Map_to_add.toString())
                    addRooms()
                }
            }
        }
    }


    private fun addRooms() {
        for (roomId in roomId) {
            val newDayId = generateRandomKey()

            // Utwórz nowy dokument w kolekcji "Days" dla danego pokoju
            val newDayDocumentRef = db.collection("rooms").document(roomId)
                .collection("Days").document(newDayId)

            // Tutaj możesz dostosować dane dla nowo utworzonego dokumentu
            val dataToSet = hashMapOf<String, Any>(
                "Day" to chosen_date,
                "Hours" to Map_to_add[roomId]!!
            )

            // Ustaw dane w dokumencie
            newDayDocumentRef.set(dataToSet)
                .addOnSuccessListener {
                    Log.d(TAG, "Nowy dzień dodany pomyślnie.")
                    // Obsługa sukcesu, jeśli to konieczne
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Błąd podczas dodawania nowego dnia", e)
                    // Obsługa błędu, jeśli to konieczne
                }
        }
    }

    fun checkRooms(){
         Log.d(TAG,generateRandomKey())//niepotrzebne do usunięcia
        db.collection("rooms")
            .get()
            .addOnSuccessListener { roomIds ->
                for(document in roomIds){
                    roomId.add(document.get("rid").toString())
                }
            }
    }
    fun generateRandomKey(): String {
        val alphanumericChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..20)
            .map { alphanumericChars[Random.nextInt(0, alphanumericChars.length)] }
            .joinToString("")
    }
    private fun displayFormattedDate(timestamp: String) {
        binding.displayDateAddRoom.text = timestamp
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        lifecycleScope.launch {
            //↓↓↓zapisuje ostatnią wybraną datę w trakcie wyboru w okienku
            calendar.set(year, month, dayOfMonth)
            //↓zmienia na cyfry
            chosen_date = formatter.format(calendar.timeInMillis)
            displayFormattedDate(chosen_date)
            //------------------------------------------------------------
        }
    }

}