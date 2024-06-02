package com.example.projekt_licencjacki

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random


private lateinit var binding: AddNewRoomsBinding
private val formatter = SimpleDateFormat("dd.MM.yyyy")
var user_email: String =""
var chosen_date: String=""
val calendar= Calendar.getInstance()
var roomId=ArrayList<String>()
var Hours: MutableMap<String, Any> = mutableMapOf()
var Map_to_add: MutableMap<String, Any> = mutableMapOf()
var db = Firebase.firestore

class add_new_rooms: AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    //kodowanie menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_details, menu)
        lifecycleScope.launch {
            val adminStatus = check_admin()

            Log.d(TAG, "admin==$adminStatus")
            if (adminStatus == true) {

                menu?.findItem(R.id.add_reset_rooms_menu_button)?.isVisible = true
                Log.d(TAG, menu?.findItem(R.id.add_reset_rooms_menu_button)?.isVisible.toString())
            } else if (adminStatus == false) {

                menu?.findItem(R.id.add_reset_rooms_menu_button)?.isVisible = false
                Log.d(TAG, menu?.findItem(R.id.add_reset_rooms_menu_button)?.isVisible.toString())
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item?.itemId==R.id.my_profile_menu_button){
            var intent= Intent(this,list_of_rooms_user_profile::class.java)
            intent.putExtra("USER",user_email)
            this.startActivity(intent)

        }
        if(item?.itemId==R.id.list_of_rooms_menu_button){
            var intent = Intent(this,list_of_rooms::class.java)
            intent.putExtra("USER",user_email)
            this.startActivity(intent)
        }
        if(item?.itemId==R.id.authors){
            var intent = Intent(this,Authors::class.java)
            intent.putExtra("USER",user_email)
            this.startActivity(intent)
        }
        if(item?.itemId==R.id.add_reset_rooms_menu_button){
            Toast.makeText(this,"You are already in Add Rooms activity", Toast.LENGTH_LONG).show()


        }
        if(item?.itemId==R.id.log_out_menu_button){
            var intent= Intent(this,login_screen::class.java)
            //↓↓↓powoduje brak możliwości powrócenia do wcześniej odpalonych activity↓↓↓
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(intent)
            //↓↓↓wyłącza aktywność aby niepotrzebnie
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
    //koniec menu
    override fun onCreate(savedInstanceState: Bundle?) {
        if(intent.hasExtra("USER")){
            user_email=intent.getStringExtra("USER")!!
            Toast.makeText(this,"Hi, $user_email",Toast.LENGTH_SHORT).show()
        }
        super.onCreate(savedInstanceState)
        binding = AddNewRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chooseDateButton.setOnClickListener{
            Log.d(TAG,"before:$ $chosen_date")
            DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(
                Calendar.DAY_OF_MONTH)).show()
            Log.d(TAG,"after : $chosen_date")
        }



        binding.confirmBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                binding.confirmBox.isChecked=true
                Hours["8:30-10:00"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
                Hours["10:10-11:40"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
                Hours["12:00-13:30"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
                Hours["13:40-15:20"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
                Hours["15:30-17:00"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
                Hours["17:10-18:40"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
                Hours["19:00-20:30"]= mutableMapOf(
                    "booked" to false,
                    "who" to ""
                )
            }else if(!binding.confirmBox.isChecked){
                Hours.clear()
            }

        }
        binding.addBtn.setOnClickListener {
            checkRooms {
                if (chosen_date.isNotEmpty()) {
                    for (i in roomId.indices) {
                        Map_to_add[roomId[i]] = Hours.toMutableMap()
                    }

                    if (Map_to_add.isEmpty()) {
                        Toast.makeText(this, "Please choose date to add", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "map to add: " + Map_to_add.toString())
                        addRooms()
                    }
                }
                binding.confirmBox.isChecked = false
            }
        }
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
    private fun addRooms() {
        if (chosen_date.isNotEmpty()) {
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

                // Sprawdź czy dany dzień już istnieje
                db.collection("rooms").document(roomId).collection("Days")
                    .whereEqualTo("Day", chosen_date)
                    .get()
                    .addOnSuccessListener { roomIds ->
                        if (roomIds.isEmpty) {
                            // Jeżeli dzień nie istnieje, dodaj nowy dokument
                            newDayDocumentRef.set(dataToSet)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Nowy dzień dodany pomyślnie.")
                                    // Obsługa sukcesu, jeśli to konieczne
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Błąd podczas dodawania nowego dnia", e)
                                    // Obsługa błędu, jeśli to konieczne
                                }
                        } else {
                            // Jeżeli dzień już istnieje, możesz dodać odpowiednie działania
                            Log.d(TAG, "Dzień już istnieje: $chosen_date")

                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Błąd podczas sprawdzania istnienia dnia", e)
                    }
            }

            // Wyczyść listę roomId po wykonaniu operacji

            roomId.clear()
        } else {
            Toast.makeText(this, "Please choose a date", Toast.LENGTH_SHORT).show()
        }
    }



    private fun checkRooms(callback: () -> Unit) {
        db.collection("rooms")
            .get()
            .addOnSuccessListener { roomIds ->
                roomId.clear()
                for (document in roomIds) {
                    roomId.add(document.getString("rid") ?: "")
                }
                callback.invoke()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting room IDs", e)
                // Handle failure if needed
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