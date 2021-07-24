package com.vsnapnewschool.network


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class ScreenOne : AppCompatActivity() {
    lateinit var lblCurrentTimeStamp : TextView
    lateinit var lblInternetAvailable : TextView
    lateinit var lblInternetSpeed : TextView
    lateinit var txtMobileNumber : EditText
    lateinit var btnUpload : Button
    lateinit var rytVisible : RelativeLayout
    var InternetAvailable:Boolean?=false

    private var myDb: SqliteDB?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_one)

        lblCurrentTimeStamp = findViewById<TextView>(R.id.lblCurrentTimeStamp)
        lblInternetAvailable = findViewById<TextView>(R.id.lblInternetAvailable)
        lblInternetSpeed = findViewById<TextView>(R.id.lblInternetSpeed)
        txtMobileNumber = findViewById<EditText>(R.id.txtMobileNumber)
        btnUpload = findViewById<Button>(R.id.btnUpload)
        rytVisible = findViewById<RelativeLayout>(R.id.rytVisible)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        InternetAvailable=true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        InternetAvailable=true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        InternetAvailable=true
                    }
                }
            }
            else{
                InternetAvailable=false
            }
        }
        else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                InternetAvailable=true
            }
            else{
                InternetAvailable=false
            }
        }

        if( InternetAvailable ==true){
            rytVisible.visibility=View.VISIBLE
            lblInternetAvailable.setText("Available")

            //Current Date and Time
            val sdf = SimpleDateFormat("dd/M/yyyy - hh:mm:ss")
            val currentDateTime = sdf.format(Date())
            System.out.println("DATE and TIME is  " + currentDateTime)
            lblCurrentTimeStamp.setText(currentDateTime)

            //Check Internet Speed
            val capabilities = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            }
            else {
                TODO("VERSION.SDK_INT < M")
            }
            var downSpeed = capabilities!!.getLinkDownstreamBandwidthKbps()
            var upSpeed  = capabilities!!.getLinkUpstreamBandwidthKbps()

            Log.d("upSpeed", upSpeed.toString())
            // Convert kbps to mbps
            var internetUpSpeed: Double? =upSpeed/1024.0
            var internetDownSpeed: Double? =downSpeed/1024.0
            lblInternetSpeed.setText(internetUpSpeed.toString() +" /mbps")

        }
        else {
            rytVisible.visibility = View.GONE
            lblInternetAvailable.setText("Not Available")

        }
        btnUpload.setOnClickListener {
           validation()
        }
    }
    private fun validation() {
        if(txtMobileNumber.text.toString().isNotEmpty() && txtMobileNumber.text.toString().length==10){
           saveUserDataToDB(txtMobileNumber.text.toString())
        }
        else{
            Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
        }

    }
    private fun saveUserDataToDB(mobile_number: String) {
        myDb = SqliteDB(this)
        if(myDb!!.checkNetworkDetails(mobile_number)){
           myDb!!.updateNetworkDetails(mobile_number,lblCurrentTimeStamp.text.toString(),lblInternetSpeed.text.toString())
        }
        else{
            myDb!!.addNetworkDetails(mobile_number,lblCurrentTimeStamp.text.toString(),lblInternetSpeed.text.toString())

        }
        Toast.makeText(this, "Your details saved successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ScreenTwo::class.java)
        startActivity(intent)
    }
}