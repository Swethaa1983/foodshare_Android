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
import com.example.foodshareapp.databinding.ActivityHistoryBinding
import com.example.foodshareapp.model.Donationresponse
import com.example.foodshareapp.model.Donations
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class History : AppCompatActivity() {
    private val b by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)


        var deamil=""

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            deamil=(getString("email", "").toString())

        }



        val p= ProgressDialog(this)
        p.show()
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.history(deamil,"history")
                .enqueue(object : Callback<Donationresponse> {
                    override fun onResponse(call: Call<Donationresponse>, response: Response<Donationresponse>) {


                        b.recyclerView.let {
                            it.layoutManager = LinearLayoutManager(this@History)
                            it.adapter= historyadapter(
                                this@History,
                                response.body()!!.user
                            )
                            p.dismiss()
                        }

                    }

                    override fun onFailure(call: Call<Donationresponse>, t: Throwable) {
                        Toast.makeText(this@History, t.message, Toast.LENGTH_SHORT).show()
                        p.dismiss()

                    }

                })
        }
    }


    class historyadapter(var context: Context, var listdata: ArrayList<Donations>): RecyclerView.Adapter<historyadapter.DataViewHolder>(){
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
            val btncall=view.findViewById<ImageView>(R.id.btncall)
            val btnlocation=view.findViewById<ImageView>(R.id.btnlocation)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardhistory, parent, false)
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
                    linearuser.visibility= View.GONE

                    tvuserdetails.setOnClickListener {
                        linearuser.visibility= View.VISIBLE
                        Handler().postDelayed({
                            linearuser.visibility= View.GONE
                        },3500)

                    }


                    btncall.setOnClickListener{
                        val intent = Intent(Intent.ACTION_CALL);
                        intent.data = Uri.parse("tel:$dnum")
                        context.startActivity(intent)
                    }
                    btnlocation.setOnClickListener{
                        val i=Intent(context,showlocation::class.java)
                        i.putExtra("city",flocate)
                        context.startActivity(i)
                    }



                }

            }

        }




        override fun getItemCount() = listdata.size
    }
}