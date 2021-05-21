package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.lang.Exception
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class LoginActivity : AppCompatActivity() {
    internal lateinit var logInButton: Button
    internal lateinit var email: EditText
    internal lateinit var password: EditText
    var loginDataOk = false
    lateinit var connect: Connection
    lateinit var connectionResult: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        logInButton = findViewById(R.id.logInButton)
        email = findViewById(R.id.editEmail)
        password = findViewById(R.id.editPassword)
        logInButton.setOnClickListener{checkIfLoginDataCorrect()}

    }

    private fun checkIfLoginDataCorrect(){
        val emailString = email.text.toString()
        val passwordString = password.text.toString()
        var st: Statement
        var correctEmail = ""
        var correctPassword = ""
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "SELECT * FROM tbl_UserInfo WHERE Email = '$emailString'"
                st = connect.createStatement()
                var rs: ResultSet = st.executeQuery(sql)
                Log.d("msg","-----------------------------------------------SUCCCEESSSSSS-------------------------------")
                rs.next()
                correctEmail = rs.getString(2)
                if(correctEmail != null){
                    correctPassword = rs.getString(3)
                    loginDataOk = passwordString == correctPassword
                }
            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

            if(loginDataOk){
                val intent = Intent(this, PairSearchActivity::class.java).apply {  }
                startActivity(intent)
            }

        }
        catch(ex: Exception){
            var isSuccess = false
            var z = ex.message.toString()
            Log.d("error", z)
        }
    }

}