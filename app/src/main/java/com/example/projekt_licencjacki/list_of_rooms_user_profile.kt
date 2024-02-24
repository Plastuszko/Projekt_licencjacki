package com.example.projekt_licencjacki

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.Calendar

class list_of_rooms_user_profile: AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    //kodowanie menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==R.id.list_of_rooms_menu_button){
            var intent= Intent(this,list_of_rooms::class.java)
            this.startActivity(intent)

        }
        if(item?.itemId==R.id.my_profile_menu_button){
            Toast.makeText(this,"You already in My prifle section", Toast.LENGTH_LONG).show()
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
    //↓↓ustawia format wyświetlanej daty
    private val formatter =SimpleDateFormat("MMM. dd, yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListOfRoomsBinding.inflate(layoutInflater)
        //↓↓↓------------------odpowiada za zapisanie do zmiennej i wyświetlenie dzisiejszej daty przy wejściu do aktywności
        var current_date=Calendar.getInstance().time
        binding.displayDate.text=formatter.format(current_date)
        //---------------------------------
        setContentView(binding.root)
        binding.chooseDateButton.setOnClickListener{
            DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()

        }

        fun reload_rooms(){//wygenerowanie aktualnej listy pokoi
            if(chosen_hour!=" "){
                val adapter = Adapter_sal(this,createroom())
                binding.listaSal.layoutManager= LinearLayoutManager(applicationContext)
                binding.listaSal.adapter=adapter
            }
        }
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



    }
    //fukncje odpowiadające za podświetlenie wybranej godziny---
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
        chosen_hour=clickedButton.text.toString()
        resetButtonColors()
        val color = ContextCompat.getColor(this,R.color.chosen)
        clickedButton.setBackgroundColor(color)
    }
    //----------------------------------------------------------

    private fun createroom():List<Sala_constructor> = buildList{
        return mutableListOf(
//            Sala_constructor(calendar.timeInMillis,chosen_hour,"exercise room"),
//            Sala_constructor(calendar.timeInMillis,chosen_hour,"exercise room"),
//            Sala_constructor(calendar.timeInMillis,chosen_hour,"exercise room")

        )


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //↓↓↓zapisuje ostatnia wybrana date w trakcie wyboru w okienku
        calendar.set(year,month,dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
        //↓sprawdza czy została już wybrana godzina, jak nie to nie załaduje sal
        if(chosen_hour!=" ") {
            val adapter = Adapter_sal(this, createroom())
            binding.listaSal.layoutManager = LinearLayoutManager(applicationContext)
            binding.listaSal.adapter = adapter
        }else{
            Toast.makeText(this,"Choose hour to show results",Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayFormattedDate(timestamp: Long){//nadpisuje tekst wybranej daty
        binding.displayDate.text=formatter.format(timestamp)
    }



}