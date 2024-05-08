package com.example.foodshareapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agri_smartempoweringfarmerswithsoilanalysis.model.RetrofitClient
import com.example.foodshareapp.databinding.ActivityProfileBinding
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class profile : AppCompatActivity() {
    private  val b by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        var id = 0;
        var type=""
        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            id = getInt("id", 0)
            b.ethname1.setText(getString("name", "").toString())
            b.ethnum1.setText(getString("mob", "").toString())
            b.ethpass1.setText(getString("pass", "").toString())
            b.ethemail1.setText(getString("email", "").toString())
            b.ethcity1.setText(getString("city", "").toString())
            type=getString("type","").toString()

        }


        b.btnupdate.setOnClickListener {
            val name =b.ethname1.text.toString()
            val num = b.ethnum1.text.toString()
            val pass =b.ethpass1.text.toString()
            val city =b.ethcity1.text.toString()
            if (num.count() == 10) {

                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.updateusers(name,num,pass,city,id,"update")
                        .enqueue(object : Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Toast.makeText(this@profile, t.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                Toast.makeText(this@profile, response.body()!!.message, Toast.LENGTH_SHORT).show()


                                getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().apply {
                                    putString("mob",num)
                                    putString("pass",pass)
                                    putString("email",b.ethemail1.text.toString().trim())
                                    putString("name",name)
                                    putString("city",city)
                                    putString("type",type)
                                    putInt("id",id)
                                    apply()
                                }

                            }
                        })
                }

                //Toast.makeText(activity, "$name,$num,$add,$city", Toast.LENGTH_SHORT).show()
            } else {
                b. ethnum1.setError("Enter number properly")
                Toast.makeText(this@profile, "Enter number properly", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
}