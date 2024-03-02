package com.example.projekt_licencjacki

import android.content.Intent
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.example.projekt_licencjacki.Authors
import com.example.projekt_licencjacki.databinding.AuthorsBinding


class Authors: AppCompatActivity() {
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
            var intent = Intent(this,list_of_rooms::class.java)
            this.startActivity(intent)

        }
        if(item?.itemId==R.id.authors){
            Toast.makeText(this,"You are already in Authors", Toast.LENGTH_LONG).show()
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

    private lateinit var binding: AuthorsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AuthorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.author1.text= "https://pl.freepik.com/ikona/komputer_8319663#fromView=search&page=1&position=21&uuid=1b51e230-a4c5-45b9-8b9c-fc330effff1b -->Ikona autorstwa Steven Edward Simanjuntak"
        binding.author2.text="https://pl.freepik.com/ikona/prezentacja_13377868#fromView=search&page=1&position=12&uuid=ae01e6a9-0630-48f7-90b1-de48ca1b9e8c -->Ikona autorstwa VectorPortal"
        binding.author3.text="https://pixabay.com/pl/users/ameliia-12205432/"
}
}