package com.sergio.tattoostudio.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.adapter.ClientListAdapter
import com.sergio.tattoostudio.entity.ClientInformation


class MainActivity : AppCompatActivity(), ClientListAdapter.OnItemClickListener {

    private lateinit var addClientFloatButton : FloatingActionButton
    private lateinit var toolbar : Toolbar
    private val db = Firebase.firestore
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val artistUID = mAuth.currentUser!!.uid
    private val clientAdapter : ClientListAdapter = ClientListAdapter(this)
    var clientList = emptyList<ClientInformation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewsById()

        setSupportActionBar(toolbar)

        val recyclerClientList = findViewById<RecyclerView>(R.id.recyclerView_client)
        recyclerClientList.apply {
            adapter = clientAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }

        configFloatActionButton()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout_button-> configLogoutButton()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        db.collection("Tattoo Artist").document(artistUID)
            .collection("Clients").get()
            .addOnSuccessListener { result ->
                clientList = result.toObjects(ClientInformation::class.java)
                clientAdapter.update(clientList)
            }
            .addOnFailureListener { exception ->
                Log.w("ADD CLIENT","Error getting document.", exception)
            }
    }


    private fun findViewsById() {
        addClientFloatButton = findViewById(R.id.floatingActionButton_addClient)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun configLogoutButton() {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
    }

    private fun configFloatActionButton() {
        addClientFloatButton.setOnClickListener {
            startActivity(Intent(this, AddClientActivity::class.java))
        }
    }

    override fun onItemClick(position: Int) {
        val clientClicked = clientList[position]
        val intent = Intent(this, ClientProfileActivity::class.java)
        intent.putExtra("Client",clientClicked)
        startActivity(intent)
    }
}