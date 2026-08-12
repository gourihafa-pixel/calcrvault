package com.calcvault.app

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<TextView>(R.id.tvCurrentPinHint).text =
            "Default PIN: 1234\nSecret vault expression: 1234= (then press =)"

        findViewById<MaterialButton>(R.id.btnChangePin).setOnClickListener { showChangePinDialog() }
        findViewById<MaterialButton>(R.id.btnReset).setOnClickListener {
            VaultManager.setPin(this, "1234")
            Toast.makeText(this, "PIN reset to 1234 and vault to 1234=", Toast.LENGTH_LONG).show()
        }
        findViewById<MaterialButton>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun showChangePinDialog() {
        val v = LayoutInflater.from(this).inflate(R.layout.dialog_change_pin, null, false)
        val old = v.findViewById<EditText>(R.id.etOldPin)
        val new1 = v.findViewById<EditText>(R.id.etNewPin)
        val new2 = v.findViewById<EditText>(R.id.etConfirmPin)
        AlertDialog.Builder(this)
            .setTitle("Change PIN")
            .setView(v)
            .setPositiveButton("Save") { _, _ ->
                if (!VaultManager.matchPin(old.text.toString())) {
                    Toast.makeText(this, "Old PIN wrong", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (new1.text.toString() != new2.text.toString()) {
                    Toast.makeText(this, "Confirmation mismatched", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (new1.text.length < 4) {
                    Toast.makeText(this, "PIN must be 4+ digits", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                VaultManager.setPin(this, new1.text.toString())
                Toast.makeText(this, "PIN updated. New vault code: ${new1.text}=", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
