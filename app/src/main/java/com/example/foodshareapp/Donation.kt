package com.example.foodshareapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agri_smartempoweringfarmerswithsoilanalysis.model.RetrofitClient
import com.example.foodshareapp.databinding.ActivityDonationBinding
import com.example.foodshareapp.model.Donationresponse
import com.example.foodshareapp.model.Donations
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Donation : AppCompatActivity() {
    private val b by lazy {
        ActivityDonationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.btnadddonation.setOnClickListener {
            startActivity(Intent(this,AddDonation::class.java))
        }
        var deamil=""

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            deamil=(getString("email", "").toString())

        }



        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.Viewdonordonatins(deamil,"Viewdonordonatins")
                .enqueue(object : Callback<Donationresponse> {
                    override fun onResponse(call: Call<Donationresponse>, response: Response<Donationresponse>) {


                        b.listdonations.let {
                            it.layoutManager = LinearLayoutManager(this@Donation)
                            it.adapter=donordonationsadapter(this@Donation,response.body()!!.user)
                            p.dismiss()
                        }

                    }

                    override fun onFailure(call: Call<Donationresponse>, t: Throwable) {
                        Toast.makeText(this@Donation, t.message, Toast.LENGTH_SHORT).show()
                        p.dismiss()

                    }

                })
        }
    }


    class donordonationsadapter(var context: Context, var listdata: ArrayList<Donations>):
        RecyclerView.Adapter<donordonationsadapter.DataViewHolder>(){
        var id=0
        inner class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val imgview=view.findViewById<ImageView>(R.id.imgview)
            val tvfname=view.findViewById<TextView>(R.id.tvfname)
            val tvdate=view.findViewById<TextView>(R.id.tvdate)
            val tvfquantlty=view.findViewById<TextView>(R.id.tvfquantlty)
            val tvstatus=view.findViewById<TextView>(R.id.tvstatus)
            val tvsource=view.findViewById<TextView>(R.id.tvsource)
            val tvdescri=view.findViewById<TextView>(R.id.tvdescri)
            val tvaddress=view.findViewById<TextView>(R.id.tvaddress)
            val tvuserdetails=view.findViewById<TextView>(R.id.tvuserdetails)
            val tvdname=view.findViewById<TextView>(R.id.tvdname)
            val tvnum=view.findViewById<TextView>(R.id.tvnum)
            val linearuser=view.findViewById<LinearLayout>(R.id.linearuser)
            val btncollected=view.findViewById<Button>(R.id.btncollected)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardviewdonations, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            holder.apply {
                listdata.get(position).apply {
                    val uri= Uri.parse(path)
                    Glide.with(context).load(uri).into(imgview)
                    tvfname.text=fname
                    tvdate.text=ftime
                    tvfquantlty.text=fquantity
                    tvstatus.text=status
                    tvsource.text=ftype
                    tvdescri.text=description
                    tvaddress.text=flocate
                    tvdname.text=name
                    tvnum.text=num
                    linearuser.visibility= View.GONE


                    if(status=="pending"){
                        linearuser.visibility= View.GONE
                        tvuserdetails.visibility=View.GONE
                        btncollected.visibility=View.GONE
                    }

                    if(status=="Completed"){
                        btncollected.visibility=View.GONE
                    }

                    tvuserdetails.setOnClickListener {
                        linearuser.visibility= View.VISIBLE
                        Handler().postDelayed({
                            linearuser.visibility= View.GONE
                        },3500)
                    }



                    btncollected.setOnClickListener {
                        if(status=="Completed"){
                            Toast.makeText(context, "Already $status", Toast.LENGTH_SHORT).show()
                        }else{
                            CoroutineScope(Dispatchers.IO).launch {
                                RetrofitClient.instance.updatecompletedstatus("Completed",id,"updatecompletedstatus")
                                    .enqueue(object: Callback<DefaultResponse> {
                                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                                            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                                        }
                                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                            Toast.makeText(context,"${response.body()!!.message} ", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }

                    }

                }

            }

        }



        override fun getItemCount() = listdata.size
    }
}