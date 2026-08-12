package com.calcvault.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VaultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vault)

        findViewById<Button>(R.id.btnFiles).setOnClickListener {
            startActivity(Intent(this, FileHiderActivity::class.java))
        }
        findViewById<Button>(R.id.btnApps).setOnClickListener {
            startActivity(Intent(this, AppHiderActivity::class.java))
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<Button>(R.id.btnLock).setOnClickListener {
            finish()
        }
    }
}
