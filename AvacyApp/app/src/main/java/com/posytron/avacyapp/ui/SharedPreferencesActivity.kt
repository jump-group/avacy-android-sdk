package com.posytron.avacyapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.posytron.avacyapp.R
import com.posytron.avacyapp.adapters.SharedPreferencesAdapter
import com.posytron.avacyapp.models.SharedPreferenceItem
import com.posytron.avacycmp.system.CMPSharedPreferencesWrapper
import java.util.*

class SharedPreferencesActivity : AppCompatActivity() {
    private val sharedPreferenceItems: ArrayList<SharedPreferenceItem> =
        ArrayList<SharedPreferenceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preferences)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val recyclerView: RecyclerView = findViewById(R.id.shared_preferences_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        val sharedPreferencesStored = CMPSharedPreferencesWrapper(this).getAll();
        for ((key, value) in sharedPreferencesStored!!.entries) {
            sharedPreferenceItems.add(SharedPreferenceItem(key, value))
        }
        recyclerView.adapter = SharedPreferencesAdapter(this, sharedPreferenceItems)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}