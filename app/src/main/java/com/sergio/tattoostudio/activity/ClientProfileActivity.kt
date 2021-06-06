package com.sergio.tattoostudio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.ClientInformation

class ClientProfileActivity : AppCompatActivity() {

    private lateinit var clientProfName : AppCompatTextView
    private lateinit var clientProfBirth : AppCompatTextView
    private lateinit var clientProfEmail : AppCompatTextView
    private lateinit var clientProfPhone : AppCompatTextView
    private lateinit var clientProfInsta : AppCompatTextView
    private lateinit var clientTattooCount : AppCompatTextView
    private lateinit var clientTattooTotalCost : AppCompatTextView
    private lateinit var clientTattooLastDate : AppCompatTextView
    private lateinit var clientTattooFirstDate : AppCompatTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_profile)



        findViewsById()

        setProfileInformation()
    }

    private fun setProfileInformation() {
        val client : ClientInformation = intent.getSerializableExtra("Client") as ClientInformation
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
    }
}