package com.example.pjiii.interfaces

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.R
import com.example.pjiii.interfaces.auth.LoginActivity

class MainPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_activityprinci)
        startActivity(Intent(this, LoginActivity::class.java))
    }
}