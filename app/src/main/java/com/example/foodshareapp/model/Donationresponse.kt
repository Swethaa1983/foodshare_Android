package com.example.foodshareapp.model

import com.example.connectinglocalworkers.model.User

class Donationresponse(val error: Boolean, val message:String, var user:ArrayList<Donations>) {
}