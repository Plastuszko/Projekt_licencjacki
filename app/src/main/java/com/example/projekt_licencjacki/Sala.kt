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
            var user_email=intent.hasExtra("USER")
        }
        if(intent.hasExtra("ROOM_ID")){
            room_id=intent.getStringExtra("ROOM_ID")!!
            Log.d(TAG,room_id)
            check_data(chosen_hour)
        }
        binding.bookARoomButton.setOnClickListener(){
            check_data(chosen_hour)
            if(binding.bookARoomButton.text=="BOOK A ROOM"){
                binding.bookARoomButton.text="BOOKED"
                val color = ContextCompat.getColor(this,R.color.chosen)
                binding.bookARoomButton.setBackgroundColor(color)
            }
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

                    // Sprawdź, czy dokument zawiera wybraną godzinę
                    if (documentData.containsKey("Hours")) {
                        val hoursData = documentData["Hours"] as? Map<String, Map<String, Any>>

                        // Sprawdź, czy wybrana godzina istnieje w danych
                        if (hoursData?.containsKey(chosen_hour) == true) {
                            val chosenHourData = hoursData[chosen_hour] as? Map<String, Any>

                            // Loguj dane tylko dla wybranej godziny
                            Log.d(TAG, "Document data for $chosen_hour: $chosenHourData")
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
