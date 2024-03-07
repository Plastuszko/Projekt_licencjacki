package com.example.projekt_licencjacki

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.projekt_licencjacki.databinding.SalaViewBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Sala: AppCompatActivity() {
    //kodowanie menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_details, menu)
        lifecycleScope.launch {
            val adminStatus = check_admin()

            Log.d(TAG, "admin==$adminStatus")
            if (adminStatus == true) {

                menu?.findItem(R.id.add_rooms)?.isVisible = true
                Log.d(TAG, menu?.findItem(R.id.add_rooms)?.isVisible.toString())
            } else if (adminStatus == false) {

                menu?.findItem(R.id.add_rooms)?.isVisible = false
                Log.d(TAG, menu?.findItem(R.id.add_rooms)?.isVisible.toString())
            }
        }
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
    var room_status:Boolean = false
    var booked_new:Boolean = false
    var current_date=""
    var chosen_hour=""
    var user_email=""
    lateinit var program_list:List<String>
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
            uploadPrograms(room_id)
            check_data(chosen_hour,current_date)
        }
        binding.bookARoomButton.setOnClickListener(){
            check_data(chosen_hour,current_date)
            if(binding.bookARoomButton.text=="CANCEL RESERVATION"&&room_status){
                booked_new=false
                change_data(chosen_hour,current_date,booked_new,"")
            }else if(binding.bookARoomButton.text=="BOOKED"){
                Toast.makeText(this,"This room is reserved by someone else",Toast.LENGTH_SHORT).show()

            }else if(binding.bookARoomButton.text=="BOOK A ROOM"&&!room_status){
                booked_new=true
                change_data(chosen_hour,current_date,booked_new,user_email)
            }

        }




}

    private fun check_data(chosen_hour: String, expectedDay: String) {
        Log.d(TAG, "Checking data from room_id: $room_id, chosen_hour: $chosen_hour, expectedDay: $expectedDay")

        db.collection("rooms").document(room_id).collection("Days")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Pobierz dane dokumentu
                    val documentData = document.data

                    // Sprawdź, czy dokument zawiera klucz "Day"
                    if (documentData.containsKey("Day")) {
                        val day = documentData["Day"] as? String

                        if (day == expectedDay) {
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
                                    updateButtonState(booked, who)
                                    room_status=booked!!
                                    // Aktualizuj przycisk na podstawie wyników

                                } else {
                                    Log.d(TAG, "Chosen hour $chosen_hour not found in document")
                                }
                            } else {
                                Log.d(TAG, "Document does not contain 'Hours' data")
                            }
                        }
                    } else {
                        Log.d(TAG, "Document does not contain 'Day' data")
                    }
                }
            }
    }

    private fun change_data(chosen_hour: String, expectedDay: String, newBooked: Boolean, newWho: String) {
        Log.d(TAG, "Changing data for room_id: $room_id, chosen_hour: $chosen_hour, expectedDay: $expectedDay")

        db.collection("rooms").document(room_id).collection("Days")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Pobierz dane dokumentu
                    val documentData = document.data

                    // Sprawdź, czy dokument zawiera klucz "Hours" i "Day"
                    if (documentData.containsKey("Hours") && documentData.containsKey("Day")) {
                        val day = documentData["Day"] as? String

                        if (day == expectedDay) {
                            val hoursData = documentData["Hours"] as? MutableMap<String, Map<String, Any>>

                            // Sprawdź, czy istnieje klucz, który zawiera wybraną godzinę
                            val matchingHourKey = hoursData?.keys?.find { it.contains(chosen_hour) }

                            if (matchingHourKey != null) {
                                // Zaktualizuj dane w mapie
                                val chosenHourData = hoursData[matchingHourKey] as? MutableMap<String, Any>

                                // Sprawdź, czy 'who' jest takie samo jak 'user_email' przed dokonaniem zmiany
                                val currentWho = chosenHourData?.get("who") as? String
                                if (currentWho == user_email||currentWho=="") {
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
                                            updateButtonState(newBooked, newWho)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Error updating data", e)
                                        }
                                } else {
                                    Log.d(TAG, "Current 'who' value is not equal to 'user_email'")
                                }
                            } else {
                                Log.d(TAG, "Chosen hour $chosen_hour not found in document")
                            }
                        }
                    } else {
                        Log.d(TAG, "Document does not contain 'Hours' or 'Day' data")
                    }
                }
                check_data(chosen_hour, current_date)
            }
    }

    private fun updateButtonState(booked: Boolean?, who: String?) {
        when {
            booked == true && who == user_email -> {
                Log.d(TAG, "WORKS CANCEL RESERVATION")
                binding.bookARoomButton.text = "CANCEL RESERVATION"
                val color = ContextCompat.getColor(this, R.color.chosen)
                binding.bookARoomButton.setBackgroundColor(color)
            }
            booked == true && who != user_email -> {
                Log.d(TAG, "WORKS BOOKED")
                binding.bookARoomButton.text = "BOOKED"
                val color = ContextCompat.getColor(this, R.color.busy)
                binding.bookARoomButton.setBackgroundColor(color)
            }
            booked == false -> {
                Log.d(TAG, "WORKS BOOK A ROOM")
                binding.bookARoomButton.text = "BOOK A ROOM"
                val color = ContextCompat.getColor(this, R.color.BOOK_A_ROOM)
                binding.bookARoomButton.setBackgroundColor(color)
            }
        }
    }
    private fun uploadPrograms(room_id: String) {
        db.collection("rooms").document(room_id).get()
            .addOnSuccessListener { roomDocument ->
                val programs = roomDocument.get("Programs") as? List<String>

                if (programs != null) {
                    // Tutaj możesz zrobić coś z listą programów, np. przypisać do odpowiedniej mapy
                    // Możesz również użyć innej struktury danych, np. MutableList, aby przechowywać programy
                    for (program in programs) {
                        binding.programsList.append("\n$program")
                    }

                    // Przykład: Wypisanie programów dla danego pokoju
                    Log.d(TAG, "Programs for room $room_id: $programs")
                } else {
                    Log.d(TAG, "No programs found for room $room_id")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch programs for room $room_id: ${exception.message}", exception)
            }
    }

    private suspend fun check_admin(): Boolean {
        return suspendCoroutine { continuation ->
            db.collection("users")
                .whereEqualTo("email", user_email)
                .get()
                .addOnSuccessListener { userData ->
                    for (user in userData) {
                        val adminStatus = user.getBoolean("admin") ?: false
                        Log.d(TAG, "Uprawnienia użytkownika: $adminStatus")
                        continuation.resume(adminStatus)
                        return@addOnSuccessListener
                    }
                    continuation.resume(false) // If no user is found or admin field is not present
                }
                .addOnFailureListener { exception ->
                    continuation.resume(false)
                }
        }
    }


}
