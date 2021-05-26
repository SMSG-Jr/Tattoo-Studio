package com.sergio.tattoostudio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.adapter.ClientListAdapter
import com.sergio.tattoostudio.model.Client

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clientList = arrayListOf<Client>()
        clientList.add(Client("", "", "", "", "", "", ))

        val recyclerClientList = findViewById<RecyclerView>(R.id.recyclerView_client)
        recyclerClientList.layoutManager = LinearLayoutManager(this)
        recyclerClientList.adapter = ClientListAdapter(clientList)

    }
}