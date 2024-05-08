package com.example.agri_smartempoweringfarmerswithsoilanalysis.model

import com.example.foodshareapp.model.Donationresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")
    fun register(
       @Field("name") name:String,
       @Field("mobile") mobile:String,
       @Field("email") email:String,
       @Field("city") city:String,
       @Field("password") password:String,
       @Field("condition") condition: String,
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun login(
        @Field("email") email: String, @Field("password") password: String,
        @Field("condition") condition: String
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun updateusers(
        @Field("name") name: String,
        @Field("mobile") mobile:String,
        @Field("password") password: String,
        @Field("city") city: String,
        @Field("id") id:Int,
        @Field("condition") condition: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("donation.php")
    fun adddonation(
        @Field("ftype") ftype:String,
        @Field("fname") fname:String,
        @Field("flocate")flocate:String,
        @Field("ftime")ftime :String,
        @Field("fquantity") fquantity:String,
        @Field("deamil") deamil:String,
        @Field("dnum")dnum :String,
        @Field("dname")dname:String,
        @Field("dcity") dcity:String,
        @Field("email")email :String,
        @Field("num") num:String,
        @Field("name")name:String,
        @Field("path") path :String,
        @Field("status") status:String,
        @Field("description")description:String,
        @Field("condition") condition:String,
        ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("donation.php")
    fun Viewdonordonatins(
        @Field("deamil") deamil:String,

        @Field("condition") condition:String,
    ): Call<Donationresponse>




    @FormUrlEncoded
    @POST("donation.php")
    fun Viewcity(
        @Field("dcity") dcity:String,
        @Field("status") status:String,
        @Field("condition") condition:String,
    ): Call<Donationresponse>



    @FormUrlEncoded
    @POST("donation.php")
    fun updatestatus(
        @Field("email")email :String,
        @Field("num") num:String,
        @Field("name")name:String,
        @Field("status") status:String,
        @Field("id") id:Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("donation.php")
    fun history(
        @Field("email")email :String,
        @Field("condition") condition:String,
    ): Call<Donationresponse>

    @FormUrlEncoded
    @POST("donation.php")
    fun updatecompletedstatus(
        @Field("status") status:String,
        @Field("id") id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>



}