package com.example.projekt_licencjacki.Dane

import com.example.projekt_licencjacki.R

class Sala_constructor {
    var user_email:String=""
    var room_id:String=""
    var day_id:String=""
    var room_number:String=""
    var type: String=""
    var capacity: Int=0
    var status: String=""
    var current_date: String=""
    var chosen_hour: String=""
    var ikonka_sali: Int=0


     constructor(room_number: String,capacity: Int,current_date: String,chosen_hour: String,type:String,status: String,){
         this.room_number=room_number
         this.capacity=capacity
         this.type=type
         this.current_date=current_date
         this.chosen_hour=chosen_hour
         this.status=status
         if(type=="laboratory"){
             ikonka_sali= R.mipmap.computer_icon
         }
         if(type=="lecture"){
             ikonka_sali= R.mipmap.presentation_icon
         }
     }
    constructor(room_number: String,capacity: Int,current_date: String,chosen_hour: String,type:String,status: String,room_id:String,user_email:String){
        this.user_email=user_email
        this.room_id=room_id
        this.room_number=room_number
        this.capacity=capacity
        this.type=type
        this.current_date=current_date
        this.chosen_hour=chosen_hour
        this.status=status
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