package com.nokhyun.bddexam

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val emailText: EditText by lazy { findViewById(R.id.emailText) }
    private val passwordText: EditText by lazy { findViewById(R.id.passwordText) }
    private val loginBtn: Button by lazy { findViewById(R.id.loginBtn) }
    private val status: TextView by lazy { findViewById(R.id.status) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener {
            if (isValidCredential(email = emailText.text.toString(), password = passwordText.text.toString())) {
                changeStatus("Success")
            } else {
                changeStatus("Failed")
            }
        }
    }

    private fun changeStatus(statusValue: String) {
        status.text = statusValue
    }

    private fun isValidCredential(email: String, password: String): Boolean {
        return email == "exampleExamil123@gmail.com" && password == "1234567"
    }
}