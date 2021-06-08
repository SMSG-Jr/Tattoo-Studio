package com.sergio.tattoostudio.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.adapter.TattooListAdapter
import com.sergio.tattoostudio.entity.ClientInformation
import com.sergio.tattoostudio.entity.TattooInformation

class ClientProfileActivity : AppCompatActivity(), TattooListAdapter.OnItemClickListener {

    private lateinit var clientProfName : AppCompatTextView
    private lateinit var clientProfBirth : AppCompatTextView
    private lateinit var clientProfEmail : AppCompatTextView
    private lateinit var clientProfPhone : AppCompatTextView
    private lateinit var clientProfInsta : AppCompatTextView
    private lateinit var clientTattooCount : AppCompatTextView
    private lateinit var clientTattooTotalCost : AppCompatTextView
    private lateinit var clientTattooLastDate : AppCompatTextView
    private lateinit var clientTattooFirstDate : AppCompatTextView
    private lateinit var addTattooFloatButton : FloatingActionButton
    private lateinit var editProfileButton : AppCompatImageButton
    private lateinit var client : ClientInformation

    private val db = Firebase.firestore
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val artistUID = mAuth.currentUser!!.uid

    private val tattooAdapter : TattooListAdapter = TattooListAdapter(this)
    var tattooList = emptyList<TattooInformation>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_profile)
        client = intent.getSerializableExtra("Client") as ClientInformation


        findViewsById()

        val recyclerTattooList = findViewById<RecyclerView>(R.id.recyclerView_clientTattooList)
        recyclerTattooList.apply {
            adapter = tattooAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }

        configFloatActionButton()

        configEditProfileButton()

    }


    override fun onResume() {
        super.onResume()

        setProfileInformation()

        db.collection("Tattoo Artist")
                .document(artistUID)
                    .collection("Clients")
                        .document(client.id)
                            .collection("Tattoos")
                .get()
                .addOnSuccessListener { result ->
                    tattooList = result.toObjects(TattooInformation::class.java)
                    tattooAdapter.update(tattooList)
                }
                .addOnFailureListener { exception ->
                    Log.w("ADD TATTOO","Error getting document.", exception)
                }
    }

    private fun setProfileInformation() {
        clientProfName.text = client.cname
        clientProfBirth.text = client.cbirthDate
        clientProfEmail.text = client.cemail
        clientProfPhone.text = client.cphoneNumber
        clientProfInsta.text = client.cinsta
        clientTattooCount.text = client.ctattooCount
        clientTattooTotalCost.text = client.ctattooTotalCost
        clientTattooFirstDate.text = client.cdateOfFirstTattoo
        clientTattooLastDate.text = client.cdateOfLastTattoo
    }

    private fun findViewsById() {
        clientProfName = findViewById(R.id.textView_clientProfileName)
        clientProfBirth = findViewById(R.id.textView_clientProfileBirth)
        clientProfEmail = findViewById(R.id.textView_clientProfileEmail)
        clientProfPhone = findViewById(R.id.textView_clientProfilePhone)
        clientProfInsta = findViewById(R.id.textView_clientProfileInstagram)
        clientTattooCount = findViewById(R.id.textView_clientTattooAmount)
        clientTattooTotalCost = findViewById(R.id.textView_clientProfileTattooCost)
        clientTattooFirstDate = findViewById(R.id.textView_clientProfileFirstTattoo)
        clientTattooLastDate = findViewById(R.id.textView_clientProfileLastTattoo)
        addTattooFloatButton = findViewById(R.id.floatingActionButton_addTattoo)
        editProfileButton = findViewById(R.id.imageButton_editProfile)
    }

    private fun configFloatActionButton() {
        addTattooFloatButton.setOnClickListener {
            val intent = Intent(this, AddTattooActivity::class.java)
            intent.putExtra("Client",client)
            startActivity(intent)

        }
    }

    private fun configEditProfileButton(){
        editProfileButton.setOnClickListener{
            val intent = Intent(this, EditClientProfileActivity::class.java)
            intent.putExtra("Client",client)
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {
        val tattooClicked = tattooList[position]
        val intent = Intent(this, EditTattooActivity::class.java)
        intent.putExtra("Tattoo",tattooClicked)
        intent.putExtra("Client",client)
        startActivity(intent)
    }
}