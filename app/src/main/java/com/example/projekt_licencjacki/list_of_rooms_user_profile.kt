package com.example.projekt_licencjacki

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt_licencjacki.databinding.ListOfRoomsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class list_of_rooms_user_profile: AppCompatActivity(), DatePickerDialog.OnDateSetListener {
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
            Toast.makeText(this,"You are already in your profile", Toast.LENGTH_LONG).show()


        }
        if(item?.itemId==R.id.list_of_rooms_menu_button){
            var intent= Intent(this,list_of_rooms::class.java)
            intent.putExtra("USER",user_email)
            this.startActivity(intent)
        }
        if(item?.itemId==R.id.authors){
            var intent = Intent(this,Authors::class.java)
            intent.putExtra("USER",user_email)
            this.startActivity(intent)
        }
        if(item?.itemId==R.id.add_reset_rooms_menu_button){
            var intent = Intent(this,add_new_rooms::class.java)
            intent.putExtra("USER",user_email)
            this.startActivity(intent)

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
    var user_email: String =""
    val calendar= Calendar.getInstance()
    var chosen_hour=" "
    private lateinit var binding: ListOfRoomsBinding
    var db = Firebase.firestore
    var Map_of_status: MutableMap <String,Any> = mutableMapOf()
    var Map_of_numbers: MutableMap <String,String> = mutableMapOf()
    var Map_of_types: MutableMap<String,String> = mutableMapOf()
    var Map_of_capacities: MutableMap<String,Int> = mutableMapOf()
    var room_ids=ArrayList<String>()
    //↓↓ustawia format wyświetlanej daty
    private val formatter =SimpleDateFormat("dd.MM.yyyy")
    var current_date=formatter.format(calendar.timeInMillis)
    override fun onCreate(savedInstanceState: Bundle?) {
        if(intent.hasExtra("USER")){
            user_email=intent.getStringExtra("USER")!!
            Toast.makeText(this,"Hi, $user_email",Toast.LENGTH_SHORT).show()
        }


        Log.d(TAG,"first current_date: $current_date")
        //↓↓↓pasek kodu odpowiadający za nadipsywanie danych na bieżąco
        lifecycleScope.launch {
            check_data()
            reload_rooms()
        }
        //---------------------------------------------------------------------
        super.onCreate(savedInstanceState)
        binding = ListOfRoomsBinding.inflate(layoutInflater)
        //↓↓↓------------------odpowiada za zapisanie do zmiennej i wyświetlenie dzisiejszej daty przy wejściu do aktywności
        var current_date=formatter.format(Calendar.getInstance().time)
        binding.displayDate.text=(current_date)

        //---------------------------------
        setContentView(binding.root)



        binding.chooseDateButton.setOnClickListener{
            DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        //↓↓↓wygenerowanie aktualnej listy pokoi

        //------------------------------------
        //↓↓↓działanie dla przycisku chowającego ram czasu i daty
        binding.hideHourButton.setOnClickListener(){
            if(binding.buttonHourMenu.isVisible==true&&binding.chooseDateButton.isVisible==true){
                binding.buttonHourMenu.isVisible=false
                binding.chooseDateButton.isVisible=false
                binding.hideHourButton.text="↓"
            }else{
                binding.buttonHourMenu.isVisible=true
                binding.chooseDateButton.isVisible=true
                binding.hideHourButton.text="↑"
            }

        }
        //----------------------------------------------------
        // ↓↓↓działania dla przycisków ram czasu--
        binding.hour1Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour1Button)
                reload_rooms()
            }
        }

        binding.hour2Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour2Button)
                reload_rooms()
            }
        }

        binding.hour3Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour3Button)
                reload_rooms()
            }
        }

        binding.hour4Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour4Button)
                reload_rooms()
            }
        }

        binding.hour5Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour5Button)
                reload_rooms()
            }
        }

        binding.hour6Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour6Button)
                reload_rooms()
            }
        }

        binding.hour7Button.setOnClickListener{
            lifecycleScope.launch {
                onButtonClick(binding.hour7Button)
                reload_rooms()
            }
        }

        //-----------------------------------

    }
    //↓↓↓fukncje odpowiadające za podświetlenie wybranej godziny---
    private fun resetButtonColors(){
        binding.hour1Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        binding.hour2Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        binding.hour3Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        binding.hour4Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        binding.hour5Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        binding.hour6Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
        binding.hour7Button.setBackgroundColor(ContextCompat.getColor(this,R.color.primary))
    }
    private fun onButtonClick(clickedButton: Button){
        Log.d(TAG,"before : $chosen_hour")
        chosen_hour=clickedButton.text.toString()
        Log.d(TAG,"after : $chosen_hour")
        resetButtonColors()
        val color = ContextCompat.getColor(this,R.color.chosen)
        clickedButton.setBackgroundColor(color)
    }
    //----------------------------------------------------------

    private suspend fun createroom(): List<Sala_constructor> {
        room_ids.clear()
        check_data()


        val roomsList = mutableListOf<Sala_constructor>()
        if(Map_of_status.isNotEmpty()){
            Log.d(TAG,"działa")

            Log.d(TAG,"current date: $current_date")
            for (i in room_ids.indices) {
                val room_id=room_ids[i]
                val roomNumber = Map_of_numbers[room_id]!!
                val roomCapacity = Map_of_capacities[room_id]!!
                val roomType = Map_of_types[room_id]!!
                var roomStatus=""
                Log.d(TAG, "room_status: $Map_of_status")


                val sala = Sala_constructor(
                    roomNumber,
                    roomCapacity,
                    current_date,
                    chosen_hour,
                    roomType,
                    roomStatus,
                    room_id,
                    user_email
                )
                roomsList.add(sala)

            }
        }
        //usuwa dane aby nie wyśiwetlać kila razy tej samej sali
        room_ids.clear()
        Map_of_numbers.clear()
        Map_of_capacities.clear()
        Map_of_types.clear()
        Map_of_status.clear()
        //-------------------------------------------------
        return roomsList
    }




    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        lifecycleScope.launch {
            //↓↓↓zapisuje ostatnią wybraną datę w trakcie wyboru w okienku
            calendar.set(year, month, dayOfMonth)
            //↓zmienia na cyfry
            current_date = formatter.format(calendar.timeInMillis)
            displayFormattedDate(current_date)
            //------------------------------------------------------------
            reload_rooms()
        }
    }
    //↓↓↓nadpisuje tekst wybranej daty
    private fun displayFormattedDate(timestamp: String) {
        binding.displayDate.text = timestamp
    }

    suspend fun reload_rooms() {
        Log.d(TAG, current_date)

        if (chosen_hour != " ") {
            val adapter = Adapter_sal(this, createroom())
            binding.listaSal.layoutManager = LinearLayoutManager(applicationContext)
            binding.listaSal.adapter = adapter
        } else {
            Toast.makeText(this, "Choose hour to show results", Toast.LENGTH_SHORT).show()
        }
    }
    private suspend fun fetchRoomData(roomDocument: DocumentSnapshot) {
        val room_id = roomDocument.getString("rid")
        var room_number = roomDocument.getString("room_number")
        Map_of_numbers[room_id!!] = room_number.toString()
        var room_type = roomDocument.getString("type")
        Map_of_types[room_id!!] = room_type.toString()
        var room_capacity = roomDocument.getString("capacity")
        Map_of_capacities[room_id!!] = room_capacity!!.toInt()
        room_ids.add(room_id!!)

        try {
            val daysSnapshot = db.collection("rooms/${room_id.toString()}/Days").get().await()
            for (dayDocument in daysSnapshot.documents) {
                var day_id = dayDocument.getString("did")
                var date = dayDocument.getString("Day")

                val hoursMap = dayDocument.get("Hours") as? Map<String, Map<String, Any>>
                if (hoursMap != null) {
                    for ((hour, hourData) in hoursMap) {
                        val booked = hourData["booked"] as? Boolean
                        val who = hourData["who"] as? String
                        if (current_date == date.toString() && chosen_hour == hour) {
                            if (who == user_email) {
                                if (booked != null) {
                                    Map_of_status[room_id] = booked!!
                                } else {
                                    Log.d(
                                        TAG,
                                        "Warning: 'booked' is null for room_id: $room_id, hour: $hour"
                                    )
                                }
                            }else(room_ids.remove(room_id))
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to fetch room data: ${exception.message}", exception)
        }
    }

    private suspend fun check_data() {
        try {
            val roomsSnapshot = db.collection("rooms").get().await()
            for (roomDocument in roomsSnapshot.documents) {
                fetchRoomData(roomDocument)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to fetch room data: ${exception.message}", exception)
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

