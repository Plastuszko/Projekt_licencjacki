package com.example.projekt_licencjacki

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_licencjacki.databinding.SalaViewBinding
import com.example.projekt_licencjacki.databinding.WyborSaliBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

class Sala: AppCompatActivity() {
    //kodowanie menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==R.id.list_of_rooms_menu_button){
            val intent= Intent(this,list_of_rooms::class.java)
            this.startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    //koniec menu
    private lateinit var binding: SalaViewBinding
    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    var room_id:String=""
    var db = Firebase.firestore
    var booked:Boolean = false
    var current_date=""
    var chosen_hour=""
    var user_email=""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = SalaViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        if(binding.bookARoomButton.text=="BOOK A ROOM"){
//            val color = ContextCompat.getColor(this,R.color.BOOK_A_ROOM)
//            binding.bookARoomButton.setBackgroundColor(color)
//        }

        if(intent.hasExtra("NUMER_SALI")){
            binding.roomNumber.text=intent.getStringExtra("NUMER_SALI")
        }
        if(intent.hasExtra("ILOSC_MIEJSC")){
            binding.capacity.text=binding.capacity.text.toString() +" " + intent.getStringExtra("ILOSC_MIEJSC")
        }
        if(intent.hasExtra("RODZAJ_SALI")){
            binding.typeOfRoom.text=binding.typeOfRoom.text.toString()+" "+intent.getStringExtra("RODZAJ_SALI")
        }
        if(intent.hasExtra("CHOSEN_DATE")){
            binding.displayDate.text=intent.getStringExtra("CHOSEN_DATE")
            current_date=intent.getStringExtra("CHOSEN_DATE").toString()
        }
        if(intent.hasExtra("CHOSEN_HOUR")){
                binding.displayHour.text=intent.getStringExtra("CHOSEN_HOUR")
                chosen_hour=intent.getStringExtra("CHOSEN_HOUR").toString()
        }
        if(intent.getStringExtra("RODZAJ_SALI")=="lecture"){
            binding.roomIcon.setImageResource(R.mipmap.presentation_icon)
        }
        if(intent.getStringExtra("RODZAJ_SALI")=="laboratory"){
            binding.roomIcon.setImageResource(R.mipmap.computer_icon)
        }
        if(intent.hasExtra("USER")){
            user_email=intent.getStringExtra("USER").toString()
            Log.d(TAG,"User email: $user_email")
        }
        if(intent.hasExtra("ROOM_ID")){
            room_id=intent.getStringExtra("ROOM_ID").toString()
            Log.d(TAG,room_id)
            check_data(chosen_hour)
        }
        binding.bookARoomButton.setOnClickListener(){
            check_data(chosen_hour)

        }




}

    private fun check_data(chosen_hour: String) {
        Log.d(TAG, "Checking data from room_id: $room_id")

        db.collection("rooms").document(room_id).collection("Days")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Pobierz dane dokumentu
                    val documentData = document.data

                    // Sprawdź, czy dokument zawiera klucz "Hours"
                    if (documentData.containsKey("Hours")) {
                        val hoursData = documentData["Hours"] as? Map<String, Map<String, Any>>

                        // Sprawdź, czy istnieje klucz, który zawiera wybraną godzinę
                        val matchingHourKey = hoursData?.keys?.find { it.contains(chosen_hour) }

                        if (matchingHourKey != null) {
                            val chosenHourData = hoursData[matchingHourKey] as? Map<String, Any>

                            // Pobierz wartości booked i who z wybranej godziny
                            val booked = chosenHourData?.get("booked") as? Boolean
                            val who = chosenHourData?.get("who") as? String
                            Log.d(TAG,"Chosen hour: "+ chosen_hour)
                            Log.d(TAG,"booked: $booked")
                            Log.d(TAG,"Who: $who")

                            if(booked==true&&who.toString()==(user_email)){
                                Log.d(TAG,"WORKS CANCEL RESERVATION")
                                binding.bookARoomButton.text="CANCEL RESERVATION"
                                val color =ContextCompat.getColor(this,R.color.chosen)
                                binding.bookARoomButton.setBackgroundColor(color)
                            }else if(booked==true&&who.toString()!=user_email){
                                Log.d(TAG,"WORKS BOOKED")
                                binding.bookARoomButton.text="BOOKED"
                                val color =ContextCompat.getColor(this,R.color.busy)
                                binding.bookARoomButton.setBackgroundColor(color)
                            }else if(booked==false){
                                Log.d(TAG,"WORKS BOOK A ROOM")
                                binding.bookARoomButton.text="BOOK A ROOM"
                                val color =ContextCompat.getColor(this,R.color.BOOK_A_ROOM)
                                binding.bookARoomButton.setBackgroundColor(color)
                            }
                        } else {
                            Log.d(TAG, "Chosen hour $chosen_hour not found in document")
                        }
                    } else {
                        Log.d(TAG, "Document does not contain 'Hours' data")
                    }
                }
            }
    }
    private fun change_data(chosen_hour: String, newBooked: Boolean, newWho: String) {
        Log.d(TAG, "Changing data for room_id: $room_id, chosen_hour: $chosen_hour")

        db.collection("rooms").document(room_id).collection("Days")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Pobierz dane dokumentu
                    val documentData = document.data

                    // Sprawdź, czy dokument zawiera klucz "Hours"
                    if (documentData.containsKey("Hours")) {
                        val hoursData = documentData["Hours"] as? MutableMap<String, Map<String, Any>>

                        // Sprawdź, czy istnieje klucz, który zawiera wybraną godzinę
                        val matchingHourKey = hoursData?.keys?.find { it.contains(chosen_hour) }

                        if (matchingHourKey != null) {
                            // Zaktualizuj dane w mapie
                            val chosenHourData = hoursData[matchingHourKey] as? MutableMap<String, Any>
                            chosenHourData?.apply {
                                put("booked", newBooked)
                                put("who", newWho)
                            }

                            // Zaktualizuj dane w Firestore
                            db.collection("rooms").document(room_id).collection("Days")
                                .document(document.id)
                                .update("Hours", hoursData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Data updated successfully.")
                                    // Dodaj kod aktualizacji interfejsu użytkownika (jeśli to konieczne)
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error updating data", e)
                                }
                        } else {
                            Log.d(TAG, "Chosen hour $chosen_hour not found in document")
                        }
                    } else {
                        Log.d(TAG, "Document does not contain 'Hours' data")
                    }
                }
            }
    }




}
