package com.posytron.avacyapp.ui

import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.posytron.avacyapp.R
import com.posytron.avacycmp.AvacyCMP
import com.posytron.avacycmp.system.CMPSharedPreferencesWrapper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        AvacyCMP.check(this, object : AvacyCMP.OnCMPReady() {
            override fun onError(error: String?) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
        val showConsents = findViewById<Button>(R.id.show_consents_btn)
        showConsents.setOnClickListener {
            AvacyCMP.showPreferenceCenter(this, object : AvacyCMP.OnCMPReady() {
                override fun onError(error: String?) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                }
            })
        }
        val viewPreferences = findViewById<Button>(R.id.view_preferences)
        viewPreferences.setOnClickListener {
            startActivity(Intent(this, SharedPreferencesActivity::class.java))
        }
        val deleteAll = findViewById<Button>(R.id.delete_all)
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

