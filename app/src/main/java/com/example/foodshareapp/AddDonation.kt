package com.example.foodshareapp

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.agri_smartempoweringfarmerswithsoilanalysis.model.RetrofitClient
import com.example.foodshareapp.databinding.ActivityAddDonationBinding
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddDonation : AppCompatActivity() {
    private val b by lazy {
        ActivityAddDonationBinding.inflate(layoutInflater)
    }
    var enoded=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        var deamil=""
        var dnum=""
        var dname=""
        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            dname=(getString("name", "").toString())
            dnum=(getString("mob", "").toString())
            deamil=(getString("email", "").toString())

        }


        val activity=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it!=null){
                val uri= Uri.parse(it.data!!.data.toString())
                val data= MediaStore.Images.Media.getBitmap(contentResolver,uri)
                val out= ByteArrayOutputStream()
                b.imgfood.setImageBitmap(data)
                data.compress(Bitmap.CompressFormat.PNG,100,out)
                enoded= Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
            }
        }


        b.imgfood.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type="image/*"
                activity.launch(this)
            }
        }

        ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line,
            arrayOf("Choose", "House","Hotel","Restaurants","Functions","Others")
        ).apply {
            b.spinnerfoodtype.adapter = this
        }

        b.btnadddon.setOnClickListener {
            val fname=b.etfname.text.toString().trim()
            val fQuantity=b.etfQuantity.text.toString().trim()
            val flocation=b.etflocation.text.toString().trim()
            val fdate=b.etfdate.text.toString().trim()
            val fcity=b.etfcity.text.toString().trim()
            val descri=b.etfdescription.text.toString().trim()
            val source=b.spinnerfoodtype.selectedItem.toString().trim()

            if(source=="Choose"){
                Toast.makeText(this, "choose the proper source of food", Toast.LENGTH_SHORT).show()
            }else if(fname.isEmpty()){b.etfname.error="Enter Food name"}
            else if(fQuantity.isEmpty()){b.etfQuantity.error="Enter Food Quantity"}
            else if(flocation.isEmpty()){b.etflocation.error="Enter Food Location"}
            else if(fdate.isEmpty()){b.etfdate.error="Enter Time to collect Food"}
            else if(fcity.isEmpty()){ b.etfcity.error="Enter Food City" }
            else if(descri.isEmpty()){b.etfdescription.error="Enter Donor Description"}
            else if(enoded==""){
                Toast.makeText(this, "Add the Pic of Food item", Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.adddonation(source,fname,flocation,fdate,fQuantity,deamil,dnum,dname
                    ,fcity,"","","",enoded,"pending",descri,"addfoodDonation")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                                Toast.makeText(this@AddDonation, t.message, Toast.LENGTH_SHORT).show()
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                Toast.makeText(this@AddDonation,"${response.body()!!.message} ", Toast.LENGTH_SHORT).show()
                                finish()



                            }
                        })
                }
            }
        }

    }
}