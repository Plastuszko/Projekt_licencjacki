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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt_licencjacki.Dane.Sala_constructor
import com.example.projekt_licencjacki.databinding.ListOfRoomsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar

class list_of_rooms: AppCompatActivity(), DatePickerDialog.OnDateSetListener {
//kodowanie menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==R.id.my_profile_menu_button){
            var intent= Intent(this,list_of_rooms_user_profile::class.java)
            this.startActivity(intent)

        }
        if(item?.itemId==R.id.list_of_rooms_menu_button){
            Toast.makeText(this,"You are already in list of rooms", Toast.LENGTH_LONG).show()
        }
        if(item?.itemId==R.id.authors){
            var intent = Intent(this,Authors::class.java)
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
    val calendar= Calendar.getInstance()
    var chosen_hour=" "
    private lateinit var binding: ListOfRoomsBinding
    var db = Firebase.firestore
    var rooms_numbers = ArrayList<String>()
    var rooms_types= ArrayList<String>()
    var rooms_capacities=ArrayList<String>()
    var room_status=ArrayList<Boolean>()
    var room_ids=ArrayList<String>()
    //↓↓ustawia format wyświetlanej daty
    private val formatter =SimpleDateFormat("dd.MM.yyyy")
    var current_date=formatter.format(calendar.timeInMillis)
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG,"first current_date: $current_date")
        //↓↓↓zczytuje i zapisuje do odpowiednich list zmienne z bazy danych
        check_data()


        //---------------------------------------------------------------------
        super.onCreate(savedInstanceState)
        binding = ListOfRoomsBinding.inflate(layoutInflater)
        //↓↓↓------------------odpowiada za zapisanie do zmiennej i wyświetlenie dzisiejszej daty przy wejściu do aktywności
        var current_date=formatter.format(Calendar.getInstance().time)
        binding.displayDate.text=(current_date)
        //---------------------------------
        setContentView(binding.root)



        binding.chooseDateButton.setOnClickListener{
            Log.d(TAG,"before:$ $current_date")
            DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
            Log.d(TAG,"after : $current_date")
        }
        //↓↓↓wygenerowanie aktualnej listy pokoi

        //------------------------------------
        //↓↓↓działanie dla przycisku chowającego ram czasu i daty
        binding.hideHourButton.setOnClickListener(){
            if(binding.buttonHourMenu.isVisible==true&&binding.chooseDateButton.isVisible==true){
                binding.buttonHourMenu.isVisible=false
                binding.chooseDateButton.isVisible=false
                binding.hideHourButton.text="↑"
            }else{
                binding.buttonHourMenu.isVisible=true
                binding.chooseDateButton.isVisible=true
                binding.hideHourButton.text="↓"
            }

        }
        //----------------------------------------------------
        // ↓↓↓działania dla przycisków ram czasu--
        binding.hour1Button.setOnClickListener{
            onButtonClick(binding.hour1Button)
            reload_rooms()
        }
        binding.hour2Button.setOnClickListener{
            onButtonClick(binding.hour2Button)
            reload_rooms()

        }
        binding.hour3Button.setOnClickListener{
            onButtonClick(binding.hour3Button)
            reload_rooms()
        }
        binding.hour4Button.setOnClickListener{
            onButtonClick(binding.hour4Button)
            reload_rooms()
        }
        binding.hour5Button.setOnClickListener{
            onButtonClick(binding.hour5Button)
            reload_rooms()
        }
        binding.hour6Button.setOnClickListener{
            onButtonClick(binding.hour6Button)
            reload_rooms()
        }
        binding.hour7Button.setOnClickListener{
            onButtonClick(binding.hour7Button)
            reload_rooms()
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

private fun createroom(): List<Sala_constructor> {
    val roomsList = mutableListOf<Sala_constructor>()
    check_data()
    if(room_status.isNotEmpty()){
    for (i in rooms_numbers.indices) {
        Log.d(TAG, "i= $i rooms_numbers.size: ${rooms_numbers.size}")
        val roomNumber = rooms_numbers[i]
        val roomCapacity = rooms_capacities[i].toInt()
        val roomType = rooms_types[i]
        var roomStatus=" "
        if(room_status[i]==true){
             roomStatus="unavailable"
        }else{
            roomStatus="available"
        }
        Log.d(TAG, "room_status: $room_status")


        val sala = Sala_constructor(
            roomNumber,
            roomCapacity,
            current_date,
            chosen_hour,
            roomType,
            roomStatus
        )
        roomsList.add(sala)
    }
    }else{}
    //usuwa dane aby nie wyśiwetlać kila razy tej samej sali
    rooms_numbers.clear()
    rooms_types.clear()
    rooms_capacities.clear()
    room_status.clear()
    //-------------------------------------------------
    return roomsList
}




    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //↓↓↓zapisuje ostatnia wybrana date w trakcie wyboru w okienku
        calendar.set(year,month,dayOfMonth)
        //↓zmienia na cyfry
        current_date=formatter.format(calendar.timeInMillis)
        displayFormattedDate(current_date)
        //------------------------------------------------------------
        reload_rooms()


    }
    //↓↓↓nadpisuje tekst wybranej daty
    private fun displayFormattedDate(timestamp: String){
        binding.displayDate.text=timestamp
    }
    //--------------------------------
    fun reload_rooms(){
        Log.d(TAG,current_date)

        if(chosen_hour!=" ") {
            val adapter = Adapter_sal(this, createroom())
            binding.listaSal.layoutManager = LinearLayoutManager(applicationContext)
            binding.listaSal.adapter = adapter
        }else{
            Toast.makeText(this,"Choose hour to show results",Toast.LENGTH_SHORT).show()
        }
    }
    private fun check_data() {
        val totalRooms = rooms_numbers.size
        var completedRooms = 0

        db.collection("rooms")
            .get()
            .addOnCompleteListener { roomsResult ->
                if (roomsResult.isSuccessful) {
                    for (roomDocument in roomsResult.result!!) {

                        var room_id = roomDocument.getString("rid")
                        Log.d(TAG, "room_id: " + room_id.toString())
                        var room_number = roomDocument.getString("room_number")
                        rooms_numbers.add(room_number!!)
                        var room_type = roomDocument.getString("type")
                        rooms_types.add(room_type!!)
                        var room_capacity = roomDocument.getString("capacity")
                        rooms_capacities.add(room_capacity!!)
                        room_ids.add(room_id!!)

                        db.collection("rooms/${room_id.toString()}/Days")
                            .get()
                            .addOnCompleteListener { daysResult ->
                                if (daysResult.isSuccessful) {
                                    for (dayDocument in daysResult.result!!) {
                                        var day_id = dayDocument.getString("did")
                                        var date = dayDocument.getString("Day")

                                        val hoursMap = dayDocument.get("Hours") as? Map<String, Map<String, Any>>
                                        if (hoursMap != null) {
                                            for ((hour, hourData) in hoursMap) {
                                                val booked = hourData["booked"] as? Boolean
                                                val who = hourData["who"] as? String
                                                if(current_date==date.toString()){
                                                    if (chosen_hour == hour) {

                                                        if (booked != null) {
                                                            room_status.add(booked)
                                                            Log.d(TAG,"current room: $room_number, room_id: $room_id, booked: $booked")
                                                        } else {
                                                            // Jeżeli 'booked' jest nullem, możesz dodać defaultowy status
                                                            room_status.add(false) // lub true, w zależności od Twoich potrzeb
                                                            Log.d(
                                                                TAG,
                                                                "Warning: 'booked' is null for room_id: $room_id, hour: $hour"
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                    }

                                }
                            }
                    }
                }
            }
    }
}

