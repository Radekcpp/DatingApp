package com.example.dateappinterface

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.sql.Statement


class ConfigurationActivity : AppCompatActivity() {
    internal lateinit var name: EditText
    internal lateinit var age: EditText
    internal lateinit var description: EditText
    internal lateinit var email: String
    internal lateinit var image: ImageView
    internal lateinit var pickImage: Button
    internal var imageString = ""
    internal var imageString2 = ""
    internal var imageString3 = ""
    internal var imageString4 = ""
    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        description = findViewById(R.id.description)
        email = intent.getStringExtra("email").toString()
        image = findViewById(R.id.image)
        pickImage = findViewById(R.id.loadImageButton)

        pickImage.setOnClickListener(){
            pickImageFromGallery()
        }
    }



    fun saveInfo(view: View){
        val nameString =name.text.toString()
        val ageString =age.text.toString()
        val descriptionString =description.text.toString()
        val nameSize = nameString.length
        var sex = "Male"
        if(nameString[nameSize-1] == 'a'){
            sex = "Female"
        }
        var st: Statement
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "UPDATE tbl_UserInfo SET Name = '$nameString', Age = '$ageString', Description = '$descriptionString', Sex = '$sex', PhotoBase64 = '$imageString' WHERE Email = '$email'"
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
        val intent = Intent(this, PreferencesActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(intent)
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            image.setImageURI(data?.data)
            var uri = data.data

            var bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
            var stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            var bytes = stream.toByteArray()
            imageString = Base64.encodeToString(bytes, Base64.DEFAULT)
            Log.d("IMAGE", imageString)
        }


    }

}