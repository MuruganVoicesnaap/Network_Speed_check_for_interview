package com.vsnapnewschool.network

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ScreenTwo : AppCompatActivity() {

    lateinit var lblCurrentTimeStamp: TextView
    lateinit var lblInternetSpeed: TextView
    lateinit var txtMobileNumber: EditText
    lateinit var btnGetResult: Button
    lateinit var rytVisible: RelativeLayout
    private var myDb: SqliteDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_two)

        lblCurrentTimeStamp = findViewById<TextView>(R.id.lblCurrentTimeStamp)
        lblInternetSpeed = findViewById<TextView>(R.id.lblInternetSpeed)
        txtMobileNumber = findViewById<EditText>(R.id.txtMobileNumber)
        btnGetResult = findViewById<Button>(R.id.btnGetResult)
        rytVisible = findViewById<RelativeLayout>(R.id.rytVisible)
        btnGetResult.setOnClickListener {

            if(txtMobileNumber.text.toString().isNotEmpty() && txtMobileNumber.text.toString().length==10) {
                rytVisible.visibility = View.VISIBLE
                myDb = SqliteDB(this)
                val res: Cursor = myDb!!.getNetWorkDetails(txtMobileNumber.text.toString())
                while (res.moveToNext()) {
                    val time = res.getString(1)
                    val internet_speed = res.getString(2)
                    lblCurrentTimeStamp.setText(time)
                    lblInternetSpeed.setText(internet_speed)
                }
            }
            else{
                rytVisible.visibility = View.GONE
                Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
