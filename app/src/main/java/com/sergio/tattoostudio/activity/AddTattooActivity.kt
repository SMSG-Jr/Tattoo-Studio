package com.sergio.tattoostudio.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.ClientInformation
import com.sergio.tattoostudio.entity.TattooInformation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AddTattooActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var editTextDescription: AppCompatEditText
    private lateinit var editTextName: AppCompatEditText
    private lateinit var editTextValue: AppCompatEditText

    private lateinit var tattooDescription:String
    private lateinit var tattooName:String
    private lateinit var tattooValueString:String
    private var tattooValue:Double = 0.00

    private lateinit var buttonTattooDate : AppCompatButton
    private lateinit var buttonAddTattoo : AppCompatButton

    private var dateString:String = ""
    private lateinit var client : ClientInformation
    private lateinit var id:String

    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val currentDate: Calendar = Calendar.getInstance()
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val artistUID = mAuth.currentUser!!.uid
    private lateinit var clientDbReference : DocumentReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tattoo)

        client = intent.getSerializableExtra("Client") as ClientInformation
        val clientId = client.id
        clientDbReference = db.collection("Tattoo Artist").document(artistUID).collection("Clients").document(clientId)

        clientDbReference.get().addOnSuccessListener { result->
            client = result.toObject()!!
        }

        findViewsById()

        datePickerButtonConfig()

        addTattooButtonConfig()

    }

    private fun addTattooButtonConfig() {
        buttonAddTattoo.setOnClickListener {
            getEditTextInfo()

            if (validateString(tattooDescription,editTextDescription)&&
                validateString(tattooName,editTextName)&&
                validateString(tattooValueString,editTextValue)&&
                validateDate())
                {
                    tattooValue = tattooValueString.replace(',','.').toDouble()
                    tattooValueString = "%.2f".format(tattooValue)
                    updateClientsTattooInfo()

                    Toast.makeText(this, "Tattoo added.", Toast.LENGTH_SHORT).show()
                    id = clientDbReference.collection("Tattoos").document().id
                    val tattooInfo = TattooInformation(id,"",tattooName,tattooDescription,dateString,tattooValueString)

                    clientDbReference.collection("Tattoos").document(id)
                        .set(tattooInfo)
                    val intent = Intent(this, ClientProfileActivity::class.java)
                    intent.putExtra("Client",client)
                    finish()
                } else
                    {
                        Toast.makeText(this, "An item is empty.", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun updateClientsTattooInfo() {
        if (client.isNewFirstDate(dateString,format)){
            clientDbReference.update("cdateOfFirstTattoo",dateString)
        }
        if (client.isNewLastDate(dateString,format)){
            clientDbReference.update("cdateOfLastTattoo",dateString)
        }
        var tattooCount = client.ctattooCount.toInt()
        var tattooTotalCost = client.ctattooTotalCost.replace(',','.').toDouble()
        tattooCount += 1
        tattooTotalCost += tattooValue
        clientDbReference.update("ctattooCount",tattooCount.toString())
        clientDbReference.update("ctattooTotalCost","%.2f".format(tattooTotalCost))

    }


    private fun findViewsById() {
        editTextDescription = findViewById(R.id.editText_addTattooDescription)
        editTextName = findViewById(R.id.editText_addTattooName)
        editTextValue = findViewById(R.id.editText_addTattooValue)
        buttonTattooDate = findViewById(R.id.button_tattooDatePicker)
        buttonAddTattoo = findViewById(R.id.button_tattooAdd)
    }

    private fun getEditTextInfo() {
        tattooDescription = editTextDescription.text.toString()
        tattooName = editTextName.text.toString()
        tattooValueString = editTextValue.text.toString()
    }

    private fun validateString(clientInformation: String, editTextId: AppCompatEditText): Boolean {
        if (TextUtils.isEmpty(clientInformation)) {
            editTextId.error = "This item can't be empty."
            return false
        }
        return true
    }

    private fun validateDate(): Boolean {
        if (TextUtils.isEmpty(dateString)) {
            buttonTattooDate.error = "This item can't be empty."
            return false
        }
        return true
    }

    private fun datePickerButtonConfig() {
        buttonTattooDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                this,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        dateString = format.format(date.time)
        buttonTattooDate.text = dateString
    }
}