package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun goToLoginScreen(view: View){
        val intent = Intent(this, LoginActivity::class.java).apply {  }
        startActivity(intent)
    }

    fun goToSigningScreen(view: View){
        val intent = Intent(this, SigningActivity::class.java).apply {  }
        startActivity(intent)
    }
}