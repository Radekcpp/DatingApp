package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import java.lang.Exception
import java.sql.Statement

class ConfigurationActivity : AppCompatActivity() {
    internal lateinit var name: EditText
    internal lateinit var age: EditText
    internal lateinit var description: EditText
    internal lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        description = findViewById(R.id.description)
        email = intent.getStringExtra("email").toString()
    }

    fun saveInfo(view: View){
        val nameString =name.text.toString()
        val ageString =age.text.toString()
        val descriptionString =description.text.toString()

        var st: Statement
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "UPDATE tbl_UserInfo SET Name = '$nameString', Age = '$ageString', Description = '$descriptionString' WHERE Email = '$email'"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg","-----------------------------------------------SUCCCEESSSSSS-------------------------------")

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
        val intent = Intent(this, PreferencesActivity::class.java).apply {  }
        startActivity(intent)
    }


}