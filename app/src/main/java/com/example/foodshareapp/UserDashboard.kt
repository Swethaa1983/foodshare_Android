package com.example.foodshareapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.foodshareapp.databinding.ActivityUserDashboardBinding

class UserDashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityUserDashboardBinding.inflate(layoutInflater)
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        b.tvname.setText("WELCOME "+getSharedPreferences("user", MODE_PRIVATE).getString("name", "")!!)

        b.carduserprofile.setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }
        b.linearUserDonation.setOnClickListener {
            startActivity(Intent(this, Donation::class.java))
        }
        b.cardhistory.setOnClickListener {
            startActivity(Intent(this, History::class.java))
        }
        b.carddonationrequest.setOnClickListener {
            startActivity(Intent(this,donationrequest::class.java))
        }
        b.carduserlogout.setOnClickListener {
            val alertdialog= AlertDialog.Builder(this)
            alertdialog.setTitle("LOGOUT")
            alertdialog.setIcon(R.drawable.logo)
            alertdialog.setCancelable(false)
            alertdialog.setMessage("Do you Want to Logout?")
            alertdialog.setPositiveButton("Yes"){ alertdialog, which->
                startActivity(Intent(this,Login::class.java))
                finish()
                val  shared=getSharedPreferences("user", MODE_PRIVATE)
                shared.edit().clear().apply()
                alertdialog.dismiss()
            }
            alertdialog.setNegativeButton("No"){alertdialog,which->
                Toast.makeText(this,"thank you", Toast.LENGTH_SHORT).show()
                alertdialog.dismiss()
            }
            alertdialog.show()

        }
    }
}