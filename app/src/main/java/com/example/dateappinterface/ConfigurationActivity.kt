package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ConfigurationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)
    }

    fun saveInfo(view: View){
        //TODO("Save given information (NAME, AGE, description")
        val intent = Intent(this, PairSearchActivity::class.java).apply {  }
        startActivity(intent)
    }


}