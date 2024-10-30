package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        textViewShowFile.text = intent.getStringExtra("File")
        if(intent.getBooleanExtra("Status",false)){
            textViewShowStatus.text = "Success"
        }
        else{ textViewShowStatus.text = "Failed" }
    }



}
