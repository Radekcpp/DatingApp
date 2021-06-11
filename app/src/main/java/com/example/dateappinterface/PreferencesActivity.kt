package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.google.android.material.slider.Slider

class PreferencesActivity : AppCompatActivity() {
    internal lateinit var agePreference: CrystalRangeSeekbar
    internal lateinit var setMinAge: TextView
    internal lateinit var setMaxAge: TextView
    internal lateinit var maleChecked: CheckBox
    internal lateinit var femaleChecked: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        agePreference = findViewById(R.id.rangeSeekbar)
        setMinAge = findViewById(R.id.setMinAge)
        setMaxAge = findViewById(R.id.setMaxAge)
        maleChecked = findViewById(R.id.checkMale)
        femaleChecked = findViewById(R.id.checkFemale)

        var minAge = 0
        var maxAge = 0
        agePreference.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            minAge = minValue.toInt()
            maxAge = maxValue.toInt()
            setMinAge.text = minValue.toString()
            setMaxAge.text = maxValue.toString()
        }


    }

    fun goToPairSearch(view: View){
        var ifMaleChecked = false
        var ifFemaleChecked = false
        if(maleChecked.isChecked){
            ifMaleChecked = true
        }
        if(femaleChecked.isChecked){
            ifFemaleChecked = true
        }
        val intent = Intent(this, PairSearchActivity::class.java).apply {  }
        startActivity(intent)
    }
}