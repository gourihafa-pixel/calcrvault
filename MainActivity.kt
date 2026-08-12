package com.calcvault.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private val input = StringBuilder()
    private val dec = DecimalFormat("#.##########")
    private var lastWasEquals = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById(R.id.display)
        wireButtons()
        VaultManager.init(this)
    }

    private fun wireButtons() {
        val ids = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnDot, R.id.btnPlus, R.id.btnMinus, R.id.btnMul,
            R.id.btnDiv, R.id.btnClear, R.id.btnDel, R.id.btnEq
        )
        for (id in ids) {
            val v = findViewById<Button>(id)
            v.setOnClickListener { handle(id) }
        }
    }

    private fun handle(id: Int) {
        when (id) {
            R.id.btn0 -> appendDigit("0")
            R.id.btn1 -> appendDigit("1")
            R.id.btn2 -> appendDigit("2")
            R.id.btn3 -> appendDigit("3")
            R.id.btn4 -> appendDigit("4")
            R.id.btn5 -> appendDigit("5")
            R.id.btn6 -> appendDigit("6")
            R.id.btn7 -> appendDigit("7")
            R.id.btn8 -> appendDigit("8")
            R.id.btn9 -> appendDigit("9")
            R.id.btnDot -> {
                if (!input.contains(".")) {
                    if (input.isEmpty() || input.last() in listOf('+','-','×','÷')) input.append("0.")
                    else input.append(".")
                }
            }
            R.id.btnPlus -> appendOp("+")
            R.id.btnMinus -> appendOp("-")
            R.id.btnMul -> appendOp("×")
            R.id.btnDiv -> appendOp("÷")
            R.id.btnClear -> { input.clear(); display.text = "0" }
            R.id.btnDel -> { if (input.isNotEmpty()) input.deleteCharAt(input.length-1); display.text = if (input.isEmpty()) "0" else input.toString() }
            R.id.btnEq -> evaluate()
        }
    }

    private fun appendDigit(d: String) {
        if (lastWasEquals) { input.clear(); lastWasEquals = false }
        input.append(d)
        display.text = input.toString()
    }

    private fun appendOp(op: String) {
        if (lastWasEquals) lastWasEquals = false
        if (input.isEmpty()) {
            if (op == "-") input.append(op)
            return
        }
        val last = input.last()
        if (last in listOf('+','-','×','÷')) input[input.length-1] = op[0]
        else input.append(op)
        display.text = input.toString()
    }

    private fun evaluate() {
        val expr = String(input).replace("×","*").replace("÷","/")
        if (VaultManager.matchVaultCode(expr)) {
            input.clear(); display.text = "0"
            startActivity(Intent(this, VaultActivity::class.java))
            return
        }
        if (expr.isEmpty()) return
        try {
            val result = ExpressionBuilder(expr).build().evaluate()
            display.text = dec.format(result)
            input.clear(); input.append(dec.format(result))
            lastWasEquals = true
        } catch (e: Exception) {
            display.text = getString(R.string.error)
        }
    }
}
