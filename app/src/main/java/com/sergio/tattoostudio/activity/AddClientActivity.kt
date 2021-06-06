package com.sergio.tattoostudio.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.ClientInformation
import java.text.SimpleDateFormat
import java.util.*


class AddClientActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {


    private lateinit var editTextName:AppCompatEditText
    private lateinit var editTextPhoneNumber:AppCompatEditText
    private lateinit var editTextEmail:AppCompatEditText

    private lateinit var clientName:String
    private lateinit var clientPhone:String
    private lateinit var clientEmail:String

    private lateinit var buttonBirthDate : AppCompatButton
    private lateinit var buttonAddClient : AppCompatButton

    private var dateString:String = ""

    private var currentDate: Calendar = Calendar.getInstance()
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var db = Firebase.firestore
    private var artistUID = mAuth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        findViewsById()

        datePickerButtonConfig()

        addClientButtonConfig()
    }

    private fun addClientButtonConfig() {
        buttonAddClient.setOnClickListener {
            getEditTextInfo()

            if (validateString(clientName,editTextName)&&
                validateString(clientPhone,editTextPhoneNumber)&&
                validateString(clientEmail,editTextEmail)&&
                validateDate()
            ) {
                Toast.makeText(this, "Client added.", Toast.LENGTH_SHORT).show()
                val id: String = db.collection("Tattoo Artist").document().id
                val clientInfo = ClientInformation(
                    id,
                    clientName,
                    "",
                    clientPhone,
                    clientEmail,
                    dateString,
                    "XX/XX/XXXX"
                )

                db.collection("Tattoo Artist")
                    .document(artistUID)
                    .collection("Clients")
                    .document(id)
                    .set(clientInfo)

                finish()
            } else {
                Toast.makeText(this, "An item is empty.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getEditTextInfo() {
        clientName = editTextName.text.toString()
        clientPhone = editTextPhoneNumber.text.toString()
        clientEmail = editTextEmail.text.toString()
    }

    private fun datePickerButtonConfig() {
        buttonBirthDate.setOnClickListener {
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

    private fun findViewsById() {
        buttonBirthDate = findViewById(R.id.button_tattooDatePicker)
        buttonAddClient = findViewById(R.id.button_tattooAdd)
        editTextName = findViewById(R.id.editText_addClientName)
        editTextPhoneNumber = findViewById(R.id.editText_addClientPhone)
        editTextEmail = findViewById(R.id.editText_addClientEmail)
    }

    private fun validateDate(): Boolean {
        if (TextUtils.isEmpty(dateString)) {
            buttonBirthDate.error = "This item can't be empty."
            return false
        }
        return true
    }

    private fun validateString(clientInformation: String, editTextId: AppCompatEditText): Boolean {
        if (TextUtils.isEmpty(clientInformation)) {
            editTextId.error = "This item can't be empty."
            return false
        }
        return true
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateString = format.format(date.time)
        buttonBirthDate.text = dateString
    }
}