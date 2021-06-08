package com.sergio.tattoostudio.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.ClientInformation
import com.sergio.tattoostudio.entity.TattooInformation
import java.text.SimpleDateFormat
import java.util.*

class EditTattooActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var editTextDescription: AppCompatEditText
    private lateinit var editTextName: AppCompatEditText
    private lateinit var editTextValue: AppCompatEditText

    private lateinit var tattooDescription:String
    private lateinit var tattooName:String
    private lateinit var tattooValueString:String
    private var tattooValue:Double = 0.00

    private lateinit var buttonTattooDate : AppCompatButton
    private lateinit var buttonAddTattoo : AppCompatButton
    private lateinit var buttonDeleteTattoo : AppCompatImageButton

    private var dateString:String = ""
    private lateinit var client : ClientInformation
    private lateinit var tattoo : TattooInformation

    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val artistUID = mAuth.currentUser!!.uid
    private lateinit var clientDbReference : DocumentReference
    private lateinit var tattooDbReference : DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tattoo)

        tattoo = intent.getSerializableExtra("Tattoo") as TattooInformation
        dateString = tattoo.tattooDate
        client = intent.getSerializableExtra("Client") as ClientInformation
        val clientId = client.id
        val tattooId = tattoo.id
        clientDbReference = db.collection("Tattoo Artist").document(artistUID).collection("Clients").document(clientId)
        tattooDbReference = clientDbReference.collection("Tattoos").document(tattooId)

        clientDbReference.get().addOnSuccessListener { result->
            client = result.toObject()!!
        }

        findViewsById()
        setTattooInformation()
        datePickerButtonConfig()

        saveEditsButtonConfig()


        deleteClientButtonConfig()

    }

    private fun saveEditsButtonConfig() {
        buttonAddTattoo.setOnClickListener {
            getEditTextInfo()
            if (validateString(tattooDescription, editTextDescription) &&
                validateString(tattooName, editTextName) &&
                validateString(tattooValueString, editTextValue) &&
                validateDate()
            ) {
                Toast.makeText(this, "Changes Saved.", Toast.LENGTH_SHORT).show()

                tattooValue = tattooValueString.replace(',', '.').toDouble()
                tattooValueString = "%.2f".format(tattooValue)
                tattooDbReference.update("tattooDate", dateString)
                updateClientsTattooInfo()
                tattooDbReference.update("tattooDescription", tattooDescription)
                tattooDbReference.update("tattooName", tattooName)
                tattooDbReference.update("tattooPrice", tattooValueString)

                finish()

            } else {
                Toast.makeText(this, "An item is empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteClientButtonConfig() {
        buttonDeleteTattoo.setOnClickListener {
            val confirmAlertDialog = AlertDialog.Builder(this)
            confirmAlertDialog.setTitle("Delete this Tattoo?")
            confirmAlertDialog.setMessage("All data from this tattoo will be deleted. \nDo you wish to continue.")

            confirmAlertDialog.setPositiveButton("Delete Tattoo!"){ dialog: DialogInterface, id:Int->
                deleteTattoo()
                Toast.makeText(this, "Tattoo deleted", Toast.LENGTH_SHORT).show()
            }
            confirmAlertDialog.setNegativeButton("Cancel."){ dialog: DialogInterface, id:Int ->
                dialog.dismiss()
            }

            confirmAlertDialog.show()
        }
    }

    private fun deleteTattoo() {
        findFirstTattooDate()
        findLasTattooDate()
        var tattooTotalCost = client.ctattooTotalCost.replace(',','.').toDouble()
        val tattooOldPrice = tattoo.tattooPrice.replace(',','.').toDouble()
        tattooTotalCost -= tattooOldPrice
        clientDbReference.update("ctattooTotalCost","%.2f".format(tattooTotalCost))
        var tattooCount = client.ctattooCount.toInt()
        tattooCount -= 1
        clientDbReference.update("ctattooCount",tattooCount.toString())
        tattooDbReference.delete()
        finish()
    }

    private fun updateClientsTattooInfo() {
        findFirstTattooDate()
        findLasTattooDate()
        tattooValue = tattooValueString.replace(',', '.').toDouble()
        Log.w("renato", "string:$tattooValueString    numere: $tattooValue")
        var tattooTotalCost = client.ctattooTotalCost.replace(',','.').toDouble()
        val tattooOldPrice = tattoo.tattooPrice.replace(',','.').toDouble()
        Log.w("renato","total:$tattooTotalCost antigo:$tattooOldPrice")
        tattooTotalCost = tattooTotalCost - tattooOldPrice + tattooValue
        client.ctattooTotalCost = "%.2f".format(tattooTotalCost)
        clientDbReference.update("ctattooTotalCost","%.2f".format(tattooTotalCost))



    }

    private fun findFirstTattooDate() {
        client.cdateOfFirstTattoo = ""
        clientDbReference.collection("Tattoos").get().addOnSuccessListener { result ->
            for (document in result){
                val queryTattoo = document.toObject(TattooInformation::class.java)
                val qTattooDate = queryTattoo.tattooDate
                if (client.isNewFirstDate(qTattooDate,format)){
                    client.cdateOfFirstTattoo = queryTattoo.tattooDate
                }
            }
            clientDbReference.update("cdateOfFirstTattoo",client.cdateOfFirstTattoo)
        }.addOnFailureListener { exception ->
            Log.w("HELMPE","Error getting document.", exception)
        }
    }

    private fun findLasTattooDate() {

        client.cdateOfLastTattoo =""
        clientDbReference.collection("Tattoos").get().addOnSuccessListener { result ->
            for (document in result){
                val queryTattoo = document.toObject(TattooInformation::class.java)
                val qTattooDate = queryTattoo.tattooDate
                if (client.isNewLastDate(qTattooDate,format)){
                    client.cdateOfLastTattoo = queryTattoo.tattooDate
                }
            }
            clientDbReference.update("cdateOfLastTattoo",client.cdateOfLastTattoo)
        }.addOnFailureListener { exception ->
            Log.w("HELPME","Error getting document.", exception)
        }


    }

    private fun findViewsById() {
        editTextDescription = findViewById(R.id.editText_addTattooDescription)
        editTextName = findViewById(R.id.editText_addTattooName)
        editTextValue = findViewById(R.id.editText_addTattooValue)
        buttonTattooDate = findViewById(R.id.button_tattooDatePicker)
        buttonAddTattoo = findViewById(R.id.button_tattooAdd)
        buttonDeleteTattoo = findViewById(R.id.imageButton_deleteTattoo)
    }

    private fun setTattooInformation() {
        editTextDescription.setText(tattoo.tattooDescription)
        editTextName.setText(tattoo.tattooName)
        editTextValue.setText(tattoo.tattooPrice)
        buttonTattooDate.text = tattoo.tattooDate
        buttonAddTattoo.text = "Save Changes"
        buttonDeleteTattoo.isVisible = true
        buttonDeleteTattoo.isClickable = true
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

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun datePickerButtonConfig() {
        buttonTattooDate.setOnClickListener {
            val tattooDate = Calendar.getInstance()
            tattooDate.time = format.parse(dateString)
            val datePicker = DatePickerDialog(
                this,
                this,
                tattooDate.get(Calendar.YEAR),
                tattooDate.get(Calendar.MONTH),
                tattooDate.get(Calendar.DAY_OF_MONTH)
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