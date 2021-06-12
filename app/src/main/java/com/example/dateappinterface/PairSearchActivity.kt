package com.example.dateappinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    var activeId = 0
    var idsToBeShown = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pair_search)
        nameShown = findViewById(R.id.Imie)
        age = findViewById(R.id.wiek)
        description = findViewById(R.id.opis)
        nameShown.text = "Imię"
        age.text = "Wiek"
        description.text = "Opis"
        activeId = intent.getIntExtra("ActiveID", 0)
        idsToBeShown = getIdsToShow()

        //Load users data into lists
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                for(i in idsToBeShown) {
                    var query: String = "Select * from tbl_UserInfo WHERE UserID = '$i'"
                    var st: Statement = connect.createStatement()
                    var rs: ResultSet = st.executeQuery(query)
                    while (rs.next()) {
                        names.add(rs.getString(4))
                        ages.add(rs.getInt(5).toString())
                        descriptions.add(rs.getString(6))
                    }
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

    //get IDs to show from table UsersToSwipe for current active ID
    private fun getIdsToShow():MutableList<Int> {
        var list = mutableListOf<Int>() //list of ids
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "Select idToSwipe from tbl_UsersToSwipe WHERE [id] = '$activeId'"
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