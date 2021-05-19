package com.example.dateappinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    internal lateinit var logInButton: Button
    internal lateinit var email: EditText
    internal lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        logInButton = findViewById(R.id.logInButton)
        email = findViewById(R.id.editEmail)
        password = findViewById(R.id.editPassword)
        logInButton.setOnClickListener{checkIfLoginDataCorrect()}

    }

    private fun checkIfLoginDataCorrect(){
        TODO("Check whether the given login data is in database -> if yes go to main logged screen, if not -> display message")

    }

    fun logIn(){

    }
}