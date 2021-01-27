package com.posytron.avacyapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.posytron.avacyapp.R
import com.posytron.avacyapp.models.SharedPreferenceItem

class SharedPreferencesAdapter(
    var sharedPreferences: ArrayList<SharedPreferenceItem>?
) :
    RecyclerView.Adapter<SharedPreferencesAdapter.SharedPreferenceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedPreferenceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shared_preference_row, parent, false)
        return SharedPreferenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SharedPreferenceViewHolder, position: Int) {
        var item = sharedPreferences!!.get(position)
        holder.key.text = item.key
        holder.value.text = item.value
    }

    override fun getItemCount(): Int {
        return sharedPreferences!!.size
    }

    class SharedPreferenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var key: TextView
        var value: TextView

        init {
            key = itemView.findViewById<View>(R.id.key) as TextView
            value = itemView.findViewById<View>(R.id.value) as TextView
        }
    }
}