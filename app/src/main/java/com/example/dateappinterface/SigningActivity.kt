package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.lang.Exception
import java.sql.ResultSet
import java.sql.Statement

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
        val name = ""
        val age = 0
        val description = ""
        val mail = email.text.toString()
        val pass = password.text.toString()
        var st: Statement
        if(!validateEmail(mail)){
            return
        }
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "INSERT INTO tbl_UserInfo (Email, Password, Name, Age, Description) VALUES ('$mail', '$pass', '$name', '$age', '$description')"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg","Success")

            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var isSuccess = false
            var z = ex.message.toString()
            Log.d("error", z)
        }
        val intent = Intent(this, ConfigurationActivity::class.java).apply {
            putExtra("email",email.text.toString())
        }
        startActivity(intent)
    }

    fun validateEmail(mail: String): Boolean {
        if (mail.isEmpty()){
            email.error = "Proszę wpisać mail"
            return false
        }
        if(Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            return true
        }
        else{
            email.error = "Nieprawidłowy adres email"
            return false
        }

        return false
    }


}

