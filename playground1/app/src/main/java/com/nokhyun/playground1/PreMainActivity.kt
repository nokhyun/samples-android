package com.nokhyun.playground1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class PreMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_main)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(getIntent())
        val options = Bundle()
        options.putInt("android.activity.splashScreenStyle", 1)
        finish()
        startActivity(intent, options)
    }
}