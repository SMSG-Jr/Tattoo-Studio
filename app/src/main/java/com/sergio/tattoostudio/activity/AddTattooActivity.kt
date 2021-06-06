package com.sergio.tattoostudio.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.TattooInformation
import java.text.SimpleDateFormat
import java.util.*

class AddTattooActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var editTextDescription: AppCompatEditText
    private lateinit var editTextName: AppCompatEditText
    private lateinit var editTextValue: AppCompatEditText

    private lateinit var tattooDescription:String
    private lateinit var tattooName:String
    private lateinit var tattooValue:String

    private lateinit var buttonTattooDate : AppCompatButton
    private lateinit var buttonAddTattoo : AppCompatButton

    private var dateString:String = ""
    private lateinit var clientId : String

    private var currentDate: Calendar = Calendar.getInstance()
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var db = Firebase.firestore
    private var artistUID = mAuth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tattoo)

        clientId = intent.getSerializableExtra("ClientId") as String

        findViewsById()

        datePickerButtonConfig()

        addTattooButtonConfig()

    }

    private fun addTattooButtonConfig() {
        buttonAddTattoo.setOnClickListener {
            getEditTextInfo()

            if (validateString(tattooDescription,editTextDescription)&&
                validateString(tattooName,editTextName)&&
                validateString(tattooValue,editTextValue)&&
                validateDate()) {

                Toast.makeText(this, "Tattoo added.", Toast.LENGTH_SHORT).show()
                val id: String = db.collection("Tattoo Artist").document(clientId).collection("Tattoos").document().id
                val tattooInfo = TattooInformation(id,"",tattooName,tattooDescription,dateString,tattooValue)

                db.collection("Tattoo Artist").document(artistUID)
                    .collection("Clients").document(clientId)
                    .collection("Tattoos").document(id)
                    .set(tattooInfo)

                finish()
            } else {
                Toast.makeText(this, "An item is empty.", Toast.LENGTH_SHORT).show()
            }
        }
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
        tattooValue = editTextValue.text.toString()
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
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateString = format.format(date.time)
        buttonTattooDate.text = dateString
    }
}