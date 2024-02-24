package com.example.projekt_licencjacki.Dane

import com.example.projekt_licencjacki.R

class Sala_constructor {

    var room_number:String=""
    var type: String=""
    var capacity: Int=0
    var status: String=""
    var date_of_room: Long=0
    var current_date: Long=0
    var chosen_hour: String=""
    var hour_of_room: String=""
    var ikonka_sali: Int=0


     constructor(room_number: String,capacity: Int,current_date: Long,chosen_hour: String,type:String){
         this.room_number=room_number
         this.capacity=capacity
         this.type=type
         this.current_date=current_date
         this.chosen_hour=chosen_hour
         if(type=="laboratory"){
             ikonka_sali= R.mipmap.computer_icon
         }
         if(type=="lecture"){
             ikonka_sali= R.mipmap.presentation_icon
         }
     }
     constructor(){}


//    val salaID:Int,
//    val numer_sali: String,
//    val current_date: Long,
//    val chosen_hour: String,
//    val ikonka_sali: Int
 }