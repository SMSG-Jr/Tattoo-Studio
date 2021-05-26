package com.sergio.tattoostudio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.model.Client
import java.util.ArrayList

class ClientListAdapter(private val list :ArrayList<Client>) : RecyclerView.Adapter<ClientListAdapter.ClientListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_client, parent, false)
        return ClientListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientListViewHolder, position: Int) {
        val client = list[position]
        holder.set(client)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ClientListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photo = itemView.findViewById<AppCompatImageView>(R.id.imageView_client_photo)
        val name = itemView.findViewById<AppCompatTextView>(R.id.textView_client_name)
        val tattooDate = itemView.findViewById<AppCompatTextView>(R.id.textView_tattoo_date)

        fun set(client: Client) {
            name.text = client.name
            tattooDate.text = client.dateOfLastTattoo

        }
    }
}