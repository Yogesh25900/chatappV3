package com.example.chatappv3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zegocloud.zimkit.services.ZIMKit
import im.zego.zim.enums.ZIMErrorCode

class MainActivity : AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginbutton  =findViewById<Button>(R.id.loginButton)

        loginbutton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val usernameEditText = findViewById<EditText>(R.id.usenameEditText)
        val myUserID = usernameEditText.text.toString()
        val myUsername= usernameEditText.text.toString()

        val avatarURl = "https://static.dc.com/dc/files/default_images/Char_Profile_Batman_20190116_5c3fc4b40faec2.47318964.jpg"
        if(myUsername.isEmpty()){
            Toast.makeText(this,"Please enter a username",Toast.LENGTH_SHORT).show()
            return
        }
        val editor =getSharedPreferences("myPrefs", MODE_PRIVATE).edit()
        editor.putString("userID",myUserID)
        editor.putString("userName", myUsername)
        editor.apply()
        ZIMKit.connectUser(myUserID,myUsername,avatarURl){error ->
            if(error.code!=ZIMErrorCode.SUCCESS){
                val message ="${error.message} :${error.code.value()}"
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                return@connectUser
            }
            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)

        }

    }
}