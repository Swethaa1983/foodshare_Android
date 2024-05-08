package com.example.foodshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodshareapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
   private  val b by lazy {
       ActivityMainBinding.inflate(layoutInflater)
   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)



        val type=getSharedPreferences("user", MODE_PRIVATE).getString("type", "")!!

        when (type) {

            "User"->{
                startActivity(Intent(this, UserDashboard::class.java))
                finish()
            }
            else -> {

                b.tvsignin.setOnClickListener {
                    startActivity(Intent(this,Login::class.java))
                    finish()
                }
            }
        }
    }
}