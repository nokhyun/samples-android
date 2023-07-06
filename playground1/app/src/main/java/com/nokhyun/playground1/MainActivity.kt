package com.nokhyun.playground1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.nokhyun.playground1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val screenSizeController: ScreenSizeController by lazy { ScreenSizeControllerImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        screenSizeController.topLevelScreenSizeInit(resources.getBoolean(R.bool.isTablet), binding)

//        if (resources.getBoolean(R.bool.isTablet)) {
//            logger {
//                "Tablet".also {
//                    it.show(binding.root)
//                }
//            }
//            val navView = (binding.navView as NavigationView)
//            navView.setupWithNavController(navController)
//        } else {
//            logger {
//                "Mobile".also {
//                    it.show(binding.root)
//                }
//            }
//            val navView = (binding.bottomNavView as BottomNavigationView)
//            navView.setupWithNavController(navController)
//        }

        navController.addOnDestinationChangedListener { _, desination, _ ->
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

interface ScreenSizeController {
    fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding)
}

class ScreenSizeControllerImpl : ScreenSizeController {

    private val navigationViewHelper2 = NavigationViewHelper2()

    override fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding) {
        val children = (binding.root as ViewGroup).children

        when {
            children.any { it is NavigationView } -> {
                val navigationView = navigationViewHelper2.asNavigationView<NavigationView>(children)
                initTable(navigationView)
            }

            children.any { it is BottomNavigationView } -> {
                val bottomNavigationView = navigationViewHelper2.asBottomNavigationView(children)
                initMobile(bottomNavigationView)
            }

            else -> throw NavigationViewException("Navigation view inconsistency")
        }
    }


    private inline fun<reified R> NavigationViewHelper2.asNavigationView(children: Sequence<View>): R = getNavigationView(children)
    private fun NavigationViewHelper2.asBottomNavigationView(children: Sequence<View>): BottomNavigationView = getNavigationView(children)

    private fun initTable(navigationView: NavigationView) {
        logger {
            "Tablet".also {
//                it.show()
            }
        }
    }

    private fun initMobile(bottomNavigationView: BottomNavigationView) {
        logger {
            "Mobile".also {
//                it.show()
            }
        }
    }

    private inner class NavigationViewHelper2 {
        inline fun <reified R> getNavigationView(view: Sequence<View>): R {
            return view.find { it is R }!! as R
        }
    }


    private class NavigationViewException(message: String) : Exception(message)
}

fun String.show(view: View) {
    Snackbar.make(view, this, 5000).show()
}

private fun String.logger() {
    Log.e("logloglog", this)
}

fun logger(msg: () -> Any) {
    msg().toString().logger()
}