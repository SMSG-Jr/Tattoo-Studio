package com.sergio.tattoostudio.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.TattooArtistInformation


class CreateAccountActivity : AppCompatActivity() {

    private lateinit var artistUser:String
    private lateinit var artistEmail:String
    private lateinit var artistCPF:String
    private lateinit var artistPassword:String
    private lateinit var artistVerifyPassword:String

    private lateinit var editArtistUser:AppCompatEditText
    private lateinit var editArtistEmail :AppCompatEditText
    private lateinit var editArtistCPF :AppCompatEditText
    private lateinit var editArtistPassword :AppCompatEditText
    private lateinit var editArtistVerifyPassword :AppCompatEditText

    private lateinit var createAccountButton :AppCompatButton
    private var db = Firebase.firestore
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)


        findViewsById()

        createAccountButton.setOnClickListener {
            createAccountButtonConfig()
        }
    }

    private fun createAccountButtonConfig() {
        getEditTextInfo()

        if (    validateString(artistUser, editArtistUser)
            && validateString(artistEmail, editArtistEmail)
            && validateString(artistCPF, editArtistCPF)
            && validateString(artistPassword, editArtistPassword)
            ) {
                if (verifyPassword(artistPassword, artistVerifyPassword)) {
                    Toast.makeText(this, "Account Created. Please Wait...", Toast.LENGTH_LONG).show()
                    mAuth.createUserWithEmailAndPassword(artistEmail,artistPassword)
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful){
                                    val firebaseArtistID = mAuth.currentUser!!.uid
                                    val tattooArtist = TattooArtistInformation(artistUser, artistCPF, artistEmail, artistPassword, firebaseArtistID)
                                    db.collection("Tattoo Artist").document(firebaseArtistID).set(tattooArtist)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful){
                                                    val intent = Intent(this, MainActivity::class.java)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }
                                }else{
                                    Toast.makeText(this, "Error: " + task.exception!!.message , Toast.LENGTH_SHORT).show()
                                }
                            }
                }
        } else {
            Toast.makeText(this, "Error creating account.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun verifyPassword(password: String, verifyPassword: String): Boolean {
        return if (password.length > 6){
            return if (password == verifyPassword){
                true
            } else{
                editArtistVerifyPassword.error = "Password does not match."
                false
            }
        } else{
            editArtistPassword.error = "Password must be longer than 5 characters."
            false
        }


    }

    private fun getEditTextInfo() {
        artistUser = editArtistUser.text.toString()
        artistEmail = editArtistEmail.text.toString()
        artistCPF = editArtistCPF.text.toString()
        artistPassword = editArtistPassword.text.toString()
        artistVerifyPassword = editArtistVerifyPassword.text.toString()
    }

    private fun validateString(clientInformation: String, editTextId: AppCompatEditText): Boolean {
        if (TextUtils.isEmpty(clientInformation)) {
            editTextId.error = "This item can't be empty."
            return false
        }
        return true
    }

    private fun findViewsById() {
        editArtistUser = findViewById(R.id.editText_artistUsername)
        editArtistEmail = findViewById(R.id.editText_artistEmail)
        editArtistCPF = findViewById(R.id.editText_artistCPF)
        editArtistPassword = findViewById(R.id.editText_artistPassword)
        editArtistVerifyPassword = findViewById(R.id.editText_artistVerifyPassword)
        createAccountButton = findViewById(R.id.button_finishAccount)
    }

}