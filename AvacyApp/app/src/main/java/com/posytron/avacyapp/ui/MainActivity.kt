package com.posytron.avacyapp.ui

import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.posytron.avacyapp.R
import com.posytron.avacycmp.AvacyCMP
import com.posytron.avacycmp.system.CMPSharedPreferencesWrapper

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
            AvacyCMP.check(this, url, object : AvacyCMP.OnCMPReady() {
                override fun onError(error: String?) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                }
            })
        }
        val showConsents = findViewById<Button>(R.id.show_consents_btn)
        showConsents.setOnClickListener {
            var url = inputUrl.text.toString()
            if (!url.isEmpty() && !url.startsWith("http")) {
                url = "http://" + url
            }
            AvacyCMP.showPreferenceCenter(this, url, object : AvacyCMP.OnCMPReady() {
                override fun onError(error: String?) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                }
            })
        }
        val viewPreferences = findViewById<CardView>(R.id.view_preferences)
        viewPreferences.setOnClickListener {
            startActivity(Intent(this, SharedPreferencesActivity::class.java))
        }
        val deleteAll = findViewById<CardView>(R.id.delete_all)
        deleteAll.setOnClickListener {
            CMPSharedPreferencesWrapper(this).removeAll();
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookies {
                Toast.makeText(this, getString(R.string.delete_all_success), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

