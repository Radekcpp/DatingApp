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
    var activeId = 0
    var minAgePref = 0
    var maxAgePref = 0
    var genderPref = ""
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
                //zaladuj users to swipe
                activeId = rs.getInt(1)
                getPreferences()
                saveToUsersToSwipe()
                val intent = Intent(this, PairSearchActivity::class.java).apply {
                    putExtra("ActiveID", activeId)
                }
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

    private fun getPreferences() {
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "SELECT minAgePref, maxAgePref, genderPref from tbl_Preferences WHERE [userID] = '$activeId'"
                var st:Statement = connect.createStatement()
                var rs: ResultSet = st.executeQuery(query)
                while(rs.next()){
                    minAgePref = rs.getInt(1)
                    maxAgePref = rs.getInt(2)
                    genderPref = rs.getString(3)
                }
            }
            else{
                print("CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            Log.d("error", ex.message.toString())
        }
    }

    private fun loadUsersWithPreferences(): MutableList<Int> {
        var list =  mutableListOf<Int>()
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "SELECT UserID from tbl_UserInfo WHERE [Age] >= '$minAgePref' AND [Age] <= '$maxAgePref' AND SEX = '$genderPref'"
                var st:Statement = connect.createStatement()
                var rs: ResultSet = st.executeQuery(query)
                while(rs.next()){
                    list.add(rs.getInt(1))
                }
            }
            else{
                print("CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            Log.d("error", ex.message.toString())
        }
        return list
    }

    //Save selected users that match preferences to table UsersToSwipe
    private fun saveToUsersToSwipe() {
        var dataToAdd: MutableList<Int>
        dataToAdd = loadUsersWithPreferences()
        var st: Statement
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                for(i in dataToAdd) {
                    var sql = "INSERT INTO tbl_UsersToSwipe (id, idToSwipe) VALUES ('$activeId', '$i')"
                    st = connect.createStatement()
                    st.execute(sql)
                    Log.d("msg", "Success")
                }
            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }
    }
    fun displayMessage(reason: String){
        var wrongLoginDataDialog: WrongLoginDataDialog = WrongLoginDataDialog(reason)
        wrongLoginDataDialog.show(supportFragmentManager, "Wrong data")
    }
}