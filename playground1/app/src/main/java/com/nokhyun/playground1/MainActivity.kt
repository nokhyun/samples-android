package com.nokhyun.playground1

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.nokhyun.playground1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (resources.getBoolean(R.bool.isTablet)) {
            logger {
                "Tablet".also {
                    it.show(binding.root)
                }
            }
            val navView = (binding.navView as NavigationView)
            navView.setupWithNavController(navController)
        } else {
            logger {
                "Mobile".also {
                    it.show(binding.root)
                }
            }
            val navView = (binding.bottomNavView as BottomNavigationView)
            navView.setupWithNavController(navController)
        }

        navController.addOnDestinationChangedListener{ _, desination, _ ->
            logger { desination }
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

private fun String.logger() {
    Log.e("logloglog", this)
}

fun logger(msg: () -> Any){
    msg().toString().logger()
}