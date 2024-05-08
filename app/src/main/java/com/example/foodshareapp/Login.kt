package com.example.foodshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agri_smartempoweringfarmerswithsoilanalysis.model.RetrofitClient
import com.example.foodshareapp.databinding.ActivityLoginBinding
import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val b by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        b.linearregister.setOnClickListener {
            startActivity(Intent(this,Register::class.java))
        }

        b.btnlogin.setOnClickListener {
            val email=b.etemail.text.toString().trim()
            val password=b.etpassword.text.toString().trim()
            if(email.isEmpty()){
                b.etemail.error="Enter your Email"
            }else if(password.isEmpty()){
                b.etpassword.error="Enter your password"
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.login(email,password,"login")
                        .enqueue(object: Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>, response: Response<LoginResponse>
                            ) {
                                if(!response.body()?.error!!){
                                    val type=response.body()?.user
                                    if (type!=null) {
                                        getSharedPreferences("user", MODE_PRIVATE).edit().apply {
                                            putString("mob",type.mobile)
                                            putString("pass",type.password)
                                            putString("email",type.email)
                                            putString("name",type.name)
                                            putString("city",type.city)
                                            putString("type","User")
                                            putInt("id",type.id)
                                            apply()
                                        }

                                        startActivity(Intent(this@Login,UserDashboard::class.java))
                                        finish()


                                    }
                                }else{
                                    Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()


                            }

                        })
                }


            }
        }
    }
}