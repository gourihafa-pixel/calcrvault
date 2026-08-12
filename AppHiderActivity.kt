package com.calcvault.app

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class AppHiderActivity : AppCompatActivity() {

    private val installedApps = mutableListOf<AppInfo>()
    private val hiddenSet = HiddenApps.getHidden(this).toMutableSet()
    private lateinit var adapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_hider)
        InstalledApps.refresh(installedApps, this)
        val list = findViewById<RecyclerView>(R.id.appsList)
        list.layoutManager = LinearLayoutManager(this)
        adapter = AppAdapter()
        list.adapter = adapter
        findViewById<MaterialButton>(R.id.btnBack).setOnClickListener { finish() }
    }

    inner class AppAdapter : RecyclerView.Adapter<AppAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val icon: ImageView = v.findViewById(R.id.ivIcon)
            val name: TextView = v.findViewById(R.id.tvAppName)
            val pkg: TextView = v.findViewById(R.id.tvPkg)
            val check: CheckBox = v.findViewById(R.id.cbHidden)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val info = installedApps[position]
            holder.name.text = info.label
            holder.pkg.text = info.packageName
            try {
                holder.icon.setImageDrawable(packageManager.getApplicationIcon(info.packageName))
            } catch (e: PackageManager.NameNotFoundException) {
                holder.icon.setImageResource(android.R.drawable.sym_def_app_icon)
            }
            holder.check.setOnCheckedChangeListener(null)
            holder.check.isChecked = hiddenSet.contains(info.packageName)
            holder.check.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) hiddenSet.add(info.packageName)
                else hiddenSet.remove(info.packageName)
                HiddenApps.save(this@AppHiderActivity, hiddenSet.toList())
            }
        }

        override fun getItemCount(): Int = installedApps.size
    }
}
