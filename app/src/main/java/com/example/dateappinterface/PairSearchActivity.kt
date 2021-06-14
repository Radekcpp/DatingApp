package com.example.dateappinterface

import android.content.Intent
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

class PairSearchActivity : AppCompatActivity() {
    internal lateinit var nameShown: TextView
    internal lateinit var age: TextView
    internal lateinit var description: TextView
    internal lateinit var image: ImageView
    var names = mutableListOf<String>()
    var ages = mutableListOf<String>()
    var descriptions = mutableListOf<String>()
    var ids = mutableListOf<Int>()
    var images = mutableListOf<String>()
    var counter = 0
    var activeId = 0
    var idsToBeShown = mutableListOf<Int>()
    var currentlyShownId = 0
    var latestShownId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pair_search)
        image = findViewById(R.id.avatar)
        nameShown = findViewById(R.id.ImiePary)
        age = findViewById(R.id.wiekPary)
        description = findViewById(R.id.opisPary)
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
        currentlyShownId = ids[counter]
        nameShown.text = names[counter]
        age.text = ages[counter]
        description.text = descriptions[counter]
        decodeImage(images[counter])
        counter++
    }


    //Add currently displayed pair to table YesSwiped
    fun likeUser(view: View) {
        //If user was not shown yet dont do anything (no user to like)
        if(currentlyShownId == latestShownId)
            return
        var st: Statement
//        var ifPair: Boolean = checkIfPairShouldBeCreated()



        //add currently shown user to YesSwiped database
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "INSERT INTO tbl_YesSwiped (ID, likedUserID) VALUES ('$activeId', '$currentlyShownId')"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg", "Success")
            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }

        //remove liked user from UsersToSwipe
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "DELETE FROM tbl_UsersToSwipe WHERE id = '$activeId' AND idToSwipe = '$currentlyShownId'"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg", "Success")
            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }
        //Check if pair is to be created - if yes then add pair to Pairs Database
        if(checkIfPairShouldBeCreated()){
            addPair()
        }
        latestShownId = currentlyShownId
        loadData(view)

    }

    private fun addPair() {
        var st: Statement
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "INSERT INTO tbl_Pairs (person1ID, person2ID) VALUES ('$activeId', '$currentlyShownId')"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg", "Success")
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

    //function checking whether the liked person has already liked user back - if yes create a pair
    private fun checkIfPairShouldBeCreated(): Boolean {
        //take liked id, and check YesSwiped if he/she liked ActiveID
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "Select * from tbl_YesSwiped WHERE [ID] = '$currentlyShownId' AND likedUserID = '$activeId'"
                var st:Statement = connect.createStatement()
                var rs: ResultSet = st.executeQuery(query)
                //check if result set is empty
                var shouldCreatePair = rs.isBeforeFirst
                return shouldCreatePair
            }
            else{
                Log.d("error","CONNECTION PROBLEM")
            }
        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }
        return false
    }

    fun goToViewPairs(view: View){
        val intent = Intent(this, PairsViewActivity::class.java).apply {
            putExtra("activeID", activeId)
        }
        startActivity(intent)
    }

    fun displayData(view: View) {}


    fun decodeImage(stringImage: String){
        var bytes = Base64.decode(stringImage, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        image.setImageBitmap(bitmap)
    }
}