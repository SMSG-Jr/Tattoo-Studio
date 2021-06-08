package com.sergio.tattoostudio.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.ClientInformation
import java.text.SimpleDateFormat
import java.util.*

class EditClientProfileActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var editClientName : AppCompatEditText
    private lateinit var editClientEmail : AppCompatEditText
    private lateinit var editClientPhone : AppCompatEditText
    private lateinit var editClientInstagram : AppCompatEditText
    private lateinit var buttonEditBirthDate : AppCompatButton
    private lateinit var buttonSaveEdits : AppCompatButton
    private lateinit var buttonDeleteClient : AppCompatImageButton

    private lateinit var clientName : String
    private lateinit var clientEmail : String
    private lateinit var clientPhone : String
    private lateinit var clientInstagram : String

    private lateinit var client : ClientInformation
    private var dateString:String = ""

    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private val db = Firebase.firestore
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val artistUID = mAuth.currentUser!!.uid
    private lateinit var clientDbReference : DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_client_profile)

        client = intent.getSerializableExtra("Client") as ClientInformation
        val clientId = client.id
        clientDbReference = db.collection("Tattoo Artist").document(artistUID).collection("Clients").document(clientId)
        dateString = client.cbirthDate

        findViewsById()
        setProfileInformation()
        datePickerButtonConfig()

        saveEditsButtonConfig()

        deleteClientButtonConfig()

    }



    private fun saveEditsButtonConfig() {
        buttonSaveEdits.setOnClickListener {

            getEditTextInfo()

            if (
                validateString(clientName,editClientName)&&
                validateString(clientEmail,editClientEmail)&&
                validateString(clientPhone,editClientPhone)&&
                validateString(clientInstagram,editClientInstagram)&&
                validateDate()
                ){
                Toast.makeText(this, "Changes Saved.", Toast.LENGTH_SHORT).show()
                    clientDbReference.update("cname",clientName)
                    clientDbReference.update("cemail",clientEmail)
                    clientDbReference.update("cphoneNumber",clientPhone)
                    clientDbReference.update("cinsta",clientInstagram)
                    clientDbReference.update("cbirthDate",dateString)
                    finish()

                }else {
                    Toast.makeText(this, "An item is empty.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteClientButtonConfig() {
        buttonDeleteClient.setOnClickListener {
            val confirmAlertDialog = AlertDialog.Builder(this)
            confirmAlertDialog.setTitle("Delete this Client?")
            confirmAlertDialog.setMessage("All data from this client will be deleted. \nDo you wish to continue.")

            confirmAlertDialog.setPositiveButton("Delete Client!"){ dialog: DialogInterface, id:Int->
                deleteClient()
                Toast.makeText(this, "Client deleted", Toast.LENGTH_SHORT).show()
            }
            confirmAlertDialog.setNegativeButton("Cancel."){ dialog: DialogInterface, id:Int ->
                dialog.dismiss()
            }

            confirmAlertDialog.show()
        }
    }

    private fun deleteClient() {
        clientDbReference.collection("Tattoos").get().addOnSuccessListener {
            it.forEach {element ->
                element.reference.delete()
            }
        }
        clientDbReference.delete()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun findViewsById() {
        editClientName = findViewById(R.id.editText_editClientName)
        editClientEmail = findViewById(R.id.editText_editClientEmail)
        editClientPhone = findViewById(R.id.editText_editClientPhone)
        editClientInstagram = findViewById(R.id.editText_editClientInstagram)
        buttonEditBirthDate = findViewById(R.id.button_editBirthDateButton)
        buttonSaveEdits = findViewById(R.id.button_saveEdits)
        buttonDeleteClient = findViewById(R.id.imageButton_deleteTattoo)
    }

    private fun setProfileInformation() {
        editClientName.setText(client.cname)
        editClientEmail.setText(client.cemail)
        editClientPhone.setText(client.cphoneNumber)
        editClientInstagram.setText(client.cinsta)
        buttonEditBirthDate.text = client.cbirthDate
    }

    private fun getEditTextInfo(){
        clientName = editClientName.text.toString()
        clientEmail = editClientEmail.text.toString()
        clientPhone = editClientPhone.text.toString()
        clientInstagram = editClientInstagram.text.toString()
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
            buttonEditBirthDate.error = "This item can't be empty."
            return false
        }
        return true
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun datePickerButtonConfig() {
        buttonEditBirthDate.setOnClickListener {
            val birthDate = Calendar.getInstance()
            birthDate.time = format.parse(dateString)
            val datePicker = DatePickerDialog(
                this,
                this,
                birthDate.get(Calendar.YEAR),
                birthDate.get(Calendar.MONTH),
                birthDate.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        dateString = format.format(date.time)
        buttonEditBirthDate.text = dateString
    }
}