package com.nokhyun.playground1

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.nokhyun.playground1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        if (resources.getBoolean(R.bool.isTablet)) {
            "Tablet".show(binding.root)
        } else {
            "Mobile".show(binding.root)
        }

//        binding.refresh.setOnRefreshListener(object: TestRefresh(){
//            override fun onTest(): SwipeRefreshLayout {
//                "왔니".log()
//                return binding.refresh
//            }
//        })
    }
}

fun String.show(view: View) {
    Snackbar.make(view, this, 5000).show()
}

fun String.log() {
    Log.e("logloglog", this)
}