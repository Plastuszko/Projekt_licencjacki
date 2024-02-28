package com.example.projekt_licencjacki

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.projekt_licencjacki.Dane.Sala_constructor
import com.example.projekt_licencjacki.databinding.WyborSaliBinding

class Adapter_sal(private var c: Context, private val lista_sal: List<Sala_constructor>): RecyclerView.Adapter<Adapter_sal.MyViewHolder>() {



    inner class MyViewHolder(private val binding: WyborSaliBinding): ViewHolder(binding.root){
        val room_number=binding.roomNumber
        val type_of_room=binding.typeOfRoom
        var capacity=binding.capacity
        val status_sali=binding.statusSali
        var ikonka_sali=binding.ikonkaSali


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val wyborSaliBinding = WyborSaliBinding.inflate(inflater,parent,false)
        return MyViewHolder(wyborSaliBinding)

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.status_sali.text=lista_sal[position].status
            holder.room_number.text=lista_sal[position].room_number
            holder.capacity.text=holder.capacity.text.toString()+" "+ lista_sal[position].capacity.toString()
            holder.type_of_room.text=holder.type_of_room.text.toString()+ " "+ lista_sal[position].type
            holder.ikonka_sali.setImageResource(lista_sal[position].ikonka_sali)
            holder.itemView.rootView.setOnClickListener{
                var intent=Intent(c,Sala::class.java)
                intent.putExtra("NUMER_SALI",lista_sal[position].room_number)
                intent.putExtra("ILOSC_MIEJSC",lista_sal[position].capacity.toString())
                intent.putExtra("RODZAJ_SALI",lista_sal[position].type)
                intent.putExtra("CHOSEN_DATE",lista_sal[position].current_date)
                intent.putExtra("CHOSEN_HOUR",lista_sal[position].chosen_hour)

                c.startActivity(intent)
            }

    }


    override fun getItemCount(): Int {
        return lista_sal.size
    }



}

