package com.example.foodshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agri_smartempoweringfarmerswithsoilanalysis.model.RetrofitClient
import com.example.foodshareapp.databinding.ActivityRegisterBinding
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private val b by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)


        b.btnregister.setOnClickListener {
            val name=b.etname.text.toString().trim()
            val num=b.etnumber.text.toString().trim()
            val pass=b.etpassword.text.toString().trim()
            val email=b.etemail.text.toString().trim()
            val city=b.etcity.text.toString().trim()
            if(name.isEmpty()){
                b.etname.error="Enter your Name"
            }else if(num.isEmpty()){
                b.etnumber.error="Enter your phone number"
            }else if(email.isEmpty()){
                b.etemail.error="Enter your Email"
            }else if(city.isEmpty()){
                b.etcity.error="Enter your city"
            }else if(pass.isEmpty()){
                b.etpassword.error="Enter your password"
            }else{
                if(num.length==10){
                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.register(name,num,email,city,pass,"register")
                            .enqueue(object: Callback<DefaultResponse> {
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                                    Toast.makeText(this@Register, t.message, Toast.LENGTH_SHORT).show()
                                }
                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                    Toast.makeText(this@Register,"Your Registration Successful ", Toast.LENGTH_SHORT).show()
                                    finish()
                                    startActivity(Intent(this@Register,Login::class.java))
                                    b.etname.text.clear()
                                    b.etnumber.text.clear()
                                    b.etemail.text.clear()
                                    b.etcity.text.clear()
                                    b.etpassword.text.clear()

                                }
                            })
                    }
                }else{
                    b.etnumber.error="Enter your phone number properly"
                }

            }
        }

    }
}