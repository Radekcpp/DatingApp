package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class SigningActivity : AppCompatActivity() {
    internal lateinit var email: EditText
    internal lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signing)
        email = findViewById(R.id.editEmail)
        password = findViewById(R.id.editPassword)
    }

    fun goToConfigurationScreen(view: View){
        val intent = Intent(this, ConfigurationActivity::class.java).apply {  }
        startActivity(intent)
    }
}