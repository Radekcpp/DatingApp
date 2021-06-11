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
    internal lateinit var description: TextView
    var names = mutableListOf<String>()
    var ages = mutableListOf<String>()
    var descriptions = mutableListOf<String>()
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pair_search)
        nameShown = findViewById(R.id.Imie)
        age = findViewById(R.id.wiek)
        description = findViewById(R.id.opis)
        nameShown.text = "Imię"
        age.text = "Wiek"
        description.text = "Opis"
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "Select * from tbl_UserInfo"
                var st:Statement = connect.createStatement()
                var rs:ResultSet = st.executeQuery(query)
                while(rs.next()){
                    names.add(rs.getString(4))
                    ages.add(rs.getInt(5).toString())
                    descriptions.add(rs.getString(6))
                }
            }
            else{
                print("CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){

        }

    }

    fun displayMessage(){
        var outOfPairsDialog: OutOfPairsDialog = OutOfPairsDialog()
        outOfPairsDialog.show(supportFragmentManager, "out of pairs")

    }

    fun loadData(v: View){
        if(counter == names.size){
            displayMessage()
            return
        }
        nameShown.text = names[counter]
        age.text = ages[counter]
        description.text = descriptions[counter]
        counter++
    }
}