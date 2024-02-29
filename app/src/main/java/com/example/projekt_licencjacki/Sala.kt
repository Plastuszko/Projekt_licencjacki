package com.example.projekt_licencjacki

import android.content.Intent
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_licencjacki.databinding.SalaViewBinding
import com.example.projekt_licencjacki.databinding.WyborSaliBinding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SalaViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(binding.bookARoomButton.text=="BOOK A ROOM"){
            val color = ContextCompat.getColor(this,R.color.BOOK_A_ROOM)
            binding.bookARoomButton.setBackgroundColor(color)
        }
        if(intent.hasExtra("USER")){
            var user_email=intent.hasExtra("USER")

        }
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
        }
        if(intent.hasExtra("CHOSEN_HOUR")){
                binding.displayHour.text=intent.getStringExtra("CHOSEN_HOUR")
        }
        if(intent.getStringExtra("RODZAJ_SALI")=="lecture"){
            binding.roomIcon.setImageResource(R.mipmap.presentation_icon)
        }
        if(intent.getStringExtra("RODZAJ_SALI")=="laboratory"){
            binding.roomIcon.setImageResource(R.mipmap.computer_icon)
        }

        binding.bookARoomButton.setOnClickListener(){
            if(binding.bookARoomButton.text=="BOOK A ROOM"){
                binding.bookARoomButton.text="BOOKED"
                val color = ContextCompat.getColor(this,R.color.chosen)
                binding.bookARoomButton.setBackgroundColor(color)
            }
        }




}



}
