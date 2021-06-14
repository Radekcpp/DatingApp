package com.example.dateappinterface

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.lang.Exception
import java.sql.ResultSet
import java.sql.Statement

class PairsViewActivity : AppCompatActivity() {
    lateinit var imiePary: TextView
    lateinit var wiekPary: TextView
    lateinit var opisPary: TextView
    lateinit var avatarPary: ImageView
    var activeId = 0
    var names = mutableListOf<String>()
    var ages = mutableListOf<String>()
    var descriptions = mutableListOf<String>()
    var ids = mutableListOf<Int>()
    var images = mutableListOf<String>()
    var counter = 0
    var currentlyShownId = 0
    var idsToShow = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairs_view)
        avatarPary = findViewById(R.id.pairAvatar)
        imiePary = findViewById(R.id.ImiePary)
        wiekPary = findViewById(R.id.wiekPary)
        opisPary = findViewById(R.id.opisPary)
        activeId = intent.getIntExtra("activeID", 0)
        imiePary.text = ""
        wiekPary.text = ""
        opisPary.text = ""
        idsToShow = loadPairs()
        loadPairsData(idsToShow)
    }

    //load id of people that are paired with active id
    private fun loadPairs(): MutableList<Int> {
        var list =  mutableListOf<Int>()
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "SELECT * from tbl_Pairs WHERE [person1ID] = '$activeId' OR [person2ID] = '$activeId'"
                var st: Statement = connect.createStatement()
                var rs: ResultSet = st.executeQuery(query)

                while(rs.next()){
                    if(rs.getInt(1) == activeId) {
                        list.add(rs.getInt(2))
                    }
                    else{
                        list.add(rs.getInt(1))
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
        return list
    }

    private fun loadPairsData(idsToBeShown: MutableList<Int>){
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
                        images.add(rs.getString(9))
                        ids.add(i)
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
    fun displayData(view: View){
        if (counter == names.size ){
            return
        }
        currentlyShownId = ids[counter]
        imiePary.text = names[counter]
        wiekPary.text = ages[counter]
        opisPary.text = descriptions[counter]
        decodeImage(images[counter])
        counter++
    }

    fun decodeImage(stringImage: String){
        var bytes = Base64.decode(stringImage, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        avatarPary.setImageBitmap(bitmap)
    }
}


