package com.sergio.tattoostudio.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.google.firebase.auth.FirebaseAuth
import com.sergio.tattoostudio.R

class LogInActivity : AppCompatActivity() {



    private lateinit var editEmailLogin : AppCompatEditText
    private lateinit var editPasswordLogin : AppCompatEditText

    private lateinit var emailLogin:String
    private lateinit var passwordLogin:String

    private lateinit var buttonLogin : AppCompatButton
    private lateinit var buttonCreate : AppCompatTextView
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        findViewsById()

        loginButton()

        createAccountButton()
    }

    private fun createAccountButton() {
        buttonCreate.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }

    private fun loginButton() {
        buttonLogin.setOnClickListener {

            getEditTextInfo()

            if (validateString(emailLogin,editEmailLogin) && validateString(passwordLogin,editPasswordLogin)) {
                mAuth.signInWithEmailAndPassword(emailLogin,passwordLogin)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Error: " + it.exception!!.message , Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateString(string: String, editTextId: AppCompatEditText): Boolean {
        if (TextUtils.isEmpty(string)) {
            editTextId.error = "This item can't be empty."
            return false
        }
        return true
    }

    private fun findViewsById() {
        editEmailLogin = findViewById(R.id.editText_emailLogin)
        editPasswordLogin = findViewById(R.id.editText_passwordLogin)
        buttonLogin = findViewById(R.id.button_login)
        buttonCreate = findViewById(R.id.textAction_createAccount)
    }

    private fun getEditTextInfo() {
        emailLogin = editEmailLogin.text.toString()
        passwordLogin = editPasswordLogin.text.toString()
    }

}