package com.example.foodshareapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.gsm.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agri_smartempoweringfarmerswithsoilanalysis.model.RetrofitClient
import com.example.foodshareapp.databinding.ActivityDonationrequestBinding
import com.example.foodshareapp.model.Donationresponse
import com.example.foodshareapp.model.Donations
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class donationrequest : AppCompatActivity() {
    private val b by lazy {
        ActivityDonationrequestBinding.inflate(layoutInflater)
    }
    var city=""
    var demail=""
    var name=""
    var num=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        city=getSharedPreferences("user", MODE_PRIVATE).getString("city", "")!!.toString()
        demail=getSharedPreferences("user", MODE_PRIVATE).getString("email", "")!!.toString()
        name=getSharedPreferences("user", MODE_PRIVATE).getString("name", "")!!.toString()
        num=getSharedPreferences("user", MODE_PRIVATE).getString("mob", "")!!.toString()
        b.etcity.setText(city)
        readcity(city)

       b.imgsearch.setOnClickListener {
           city=b.etcity.text.toString().trim()
           if(city.isEmpty()){
               b.etcity.error="Enter city(Eg: Tirupati)"
           }else{
               readcity(city)
           }

       }
    }
    private fun readcity(city: String) {
        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.Viewcity(city,"pending","Viewcity")
                .enqueue(object : Callback<Donationresponse> {
                    override fun onResponse(call: Call<Donationresponse>, response: Response<Donationresponse>) {

                       b.recyclerView.let {

                               it.layoutManager = LinearLayoutManager(this@donationrequest)
                               it.adapter=
                                   donationrequestsadapter(this@donationrequest, response.body()!!.user,demail,name,num)
                               p.dismiss()

                       }



                    }

                    override fun onFailure(call: Call<Donationresponse>, t: Throwable) {
                        Toast.makeText(this@donationrequest, t.message, Toast.LENGTH_SHORT).show()
                        p.dismiss()

                    }

                })
        }

    }


    class donationrequestsadapter(var context: Context, var listdata: ArrayList<Donations>, var amil:String,var ddname:String,
   var  ddnum:String): RecyclerView.Adapter<donationrequestsadapter.DataViewHolder>(){
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
            val cardvi1=view.findViewById<CardView>(R.id.cardvi1)
            val tvuserdetails=view.findViewById<TextView>(R.id.tvuserdetails)
            val tvdname=view.findViewById<TextView>(R.id.tvdname)
            val tvnum=view.findViewById<TextView>(R.id.tvnum)
            val linearuser=view.findViewById<LinearLayout>(R.id.linearuser)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.carddonationrequest, parent, false)
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
                    tvdname.text=dname
                    tvnum.text=dnum
                    linearuser.visibility=View.GONE

                    if(deamil==amil){
                        cardvi1.visibility=View.GONE
                    }


                    tvuserdetails.setOnClickListener {
                        linearuser.visibility=View.VISIBLE
                        Handler().postDelayed({
                            linearuser.visibility=View.GONE
                        },3500)

                    }
                    
                    
                    itemView.setOnClickListener {
                        if(status=="pending") {
                            val alertdialog = AlertDialog.Builder(context)
                            alertdialog.setTitle("Accept")
                            alertdialog.setIcon(R.drawable.logo)
                            alertdialog.setMessage("Do you Want to Accept the Food Donation?")
                            alertdialog.setPositiveButton("Yes") { alertdialog, which ->
                                readaccepted(id, amil, ddname, ddnum)
                                alertdialog.dismiss()
                            }

                            alertdialog.show()
                        }else{
                            Toast.makeText(context, "Already accepted", Toast.LENGTH_SHORT).show()
                        }
                    }



                }

            }

        }

        private fun readaccepted(id: Int, amil: String, ddname: String, ddnum: String) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updatestatus(amil,ddnum,ddname,"Accepted",id,"updatestatus")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context,"Your Response Noted", Toast.LENGTH_SHORT).show()
                            (context as Activity).finish()
                            context.startActivity(Intent(context,History::class.java))
                            if (TextUtils.isDigitsOnly(ddnum)) {
                                val smsManager: SmsManager = SmsManager.getDefault()
                                smsManager.sendTextMessage(ddnum, null, "Hi $ddname\n Your Donation Accepted, \n we will try to Collect as soon as possible", null, null)

                            }

                        }
                    })
            }
        }


        override fun getItemCount() = listdata.size
    }
}