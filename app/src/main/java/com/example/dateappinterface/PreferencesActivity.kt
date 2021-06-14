package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.google.android.material.slider.Slider
import java.lang.Exception
import java.sql.ResultSet
import java.sql.Statement

class PreferencesActivity : AppCompatActivity() {
    internal lateinit var agePreference: CrystalRangeSeekbar
    internal lateinit var setMinAge: TextView
    internal lateinit var setMaxAge: TextView
    internal lateinit var maleChecked: CheckBox
    internal lateinit var femaleChecked: CheckBox
    internal lateinit var email: String
    var minAge = 0
    var maxAge = 0
    var genderPref = ""
    var activeID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        agePreference = findViewById(R.id.rangeSeekbar)
        setMinAge = findViewById(R.id.setMinAge)
        setMaxAge = findViewById(R.id.setMaxAge)
        maleChecked = findViewById(R.id.checkMale)
        femaleChecked = findViewById(R.id.checkFemale)
        email = intent.getStringExtra("email").toString()

        agePreference.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            minAge = minValue.toInt()
            maxAge = maxValue.toInt()
            setMinAge.text = minValue.toString()
            setMaxAge.text = maxValue.toString()
        }
        var st: Statement
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()

            if(connect != null){
                var sql = "SELECT [UserID] FROM tbl_UserInfo WHERE Email = '$email'"
                var st:Statement = connect.createStatement()
                var rs: ResultSet = st.executeQuery(sql)
                Log.d("msg","Success")
                rs.next()
                activeID = rs.getInt(1)
                Log.d("ACTIVE ID", activeID.toString())
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


    //Save preferences and go to pair search
    fun goToPairSearch(view: View){
        var st: Statement
        var ifMaleChecked = false
        var ifFemaleChecked = false
        if(maleChecked.isChecked){
            ifMaleChecked = true
            genderPref = "Male"
        }
        if(femaleChecked.isChecked){
            ifFemaleChecked = true
            genderPref = "Female"
        }
        if(ifMaleChecked and ifFemaleChecked){
            genderPref = "Both"
        }
        if(!ifMaleChecked and !ifFemaleChecked){
            maleChecked.error = "Należy zaznaczyć conajmniej jedną opcję"
            femaleChecked.error = "Należy zaznaczyć conajmniej jedną opcję"
            return
        }
        //Save Preferences
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "INSERT INTO tbl_Preferences (userID, minAgePref, maxAgePref, genderPref) VALUES ('$activeID', '$minAge', '$maxAge', '$genderPref')"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg","Success")

            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }


        //Fill in Users to Swipe table in database
        saveToUsersToSwipe()

        //go to next activity
        val intent = Intent(this, PairSearchActivity::class.java).apply {
            putExtra("ActiveID", activeID)
        }
        startActivity(intent)
    }

    //Select all rows from UserInfo which matches the preferences
    private fun loadUsersWithPreferences(): MutableList<Int> {
        var list =  mutableListOf<Int>()
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var query: String = "SELECT UserID from tbl_UserInfo WHERE [Age] >= '$minAge' AND [Age] <= '$maxAge' AND SEX = '$genderPref'"

                if(genderPref == "Both"){
                    query = "SELECT UserID from tbl_UserInfo WHERE [Age] >= '$minAge' AND [Age] <= '$maxAge'"
                }

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
                    var sql = "INSERT INTO tbl_UsersToSwipe (id, idToSwipe) VALUES ('$activeID', '$i')"
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


}