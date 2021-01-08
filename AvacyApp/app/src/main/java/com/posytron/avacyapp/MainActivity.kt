package com.posytron.avacyapp

import android.os.Bundle
import android.webkit.WebResourceError
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.posytron.avacycmp.AvacyCMP
import com.posytron.avacycmp.OnCMPReady

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val inputUrl = findViewById<EditText>(R.id.input_url)
        val go = findViewById<Button>(R.id.go_btn)
        go.setOnClickListener {
            var url = inputUrl.text.toString()
            if (!url.isEmpty() && !url.startsWith("http")) {
                url = "http://" + url
            }
            AvacyCMP.view(this, url, object : OnCMPReady {
                override fun onError(error: WebResourceError?) {
                    Toast.makeText(
                        this@MainActivity,
                        error!!.description.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}