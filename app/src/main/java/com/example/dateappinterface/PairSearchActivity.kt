package com.example.dateappinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.lang.Exception
import java.sql.ResultSet
import java.sql.Statement

class PairSearchActivity : AppCompatActivity() {
    internal lateinit var nameShown: TextView
    internal lateinit var age: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pair_search)

        nameShown = findViewById(R.id.Imie)
        age = findViewById(R.id.wiek)

    }

    fun loadData(v: View){
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "Select * from tbl_UserInfo"
                var st:Statement = connect.createStatement()
                var rs:ResultSet = st.executeQuery(query)


                while(rs.next()){
                    nameShown.text = rs.getString(4)
                    age.text = rs.getInt(5).toString()

                }
            }
            else{
                print("CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){

        }
    }
}