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
        lateinit var rs: ResultSet
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "SELECT * FROM tbl_UserInfo WHERE Email = '$emailString'"
                st = connect.createStatement()
                rs = st.executeQuery(sql)
                Log.d("msg","-----------------------------------------------SUCCCEESSSSSS-------------------------------")
                rs.next()
                correctEmail = rs.getString(2)
                if(correctEmail != null){
                    correctPassword = rs.getString(3)
                    loginDataOk = passwordString == correctPassword  //assigns true or false to loginDataOk whether the passwords matches
                    if(!loginDataOk){
                        val reason = "password"
                        displayMessage(reason)
                    }
                }
            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

            if(loginDataOk){
                //read information about actrive user and save them into object of ActiveUser
                val userId = rs.getInt(1)
                val userEmail = rs.getString(2)
                val userPassword = rs.getString(3)
                val userName = rs.getString(4)
                val userAge = rs.getInt(5)
                val userDescription = rs.getString(6)
                var activeUser = ActiveUser(userId, userEmail, userPassword, userName, userAge, userDescription)


                val intent = Intent(this, PairSearchActivity::class.java).apply {  }
                startActivity(intent)
            }

        }
        catch(ex: Exception){
            var isSuccess = false
            var z = ex.message.toString()
            Log.d("error", z)
            val reason = "email"
            displayMessage(reason)
        }
    }


    fun displayMessage(reason: String){
        var wrongLoginDataDialog: WrongLoginDataDialog = WrongLoginDataDialog(reason)
        wrongLoginDataDialog.show(supportFragmentManager, "Wrong data")

    }
}