package com.nokhyun.playground1

import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.nokhyun.playground1.databinding.ActivityMainBinding

interface ScreenSizeController {
    fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding, navController: NavController, onResult: () -> Unit)
}

class ScreenSizeControllerImpl : ScreenSizeController {

    private val navigationViewHelper = NavigationViewHelper()
    private val bindingHelper = BindingHelper()

    override fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding, navController: NavController, onResult: () -> Unit) {
        bindings(binding, navController, isTablet)
        onResult()
    }

    private inline fun <reified R> NavigationViewHelper.asNavigationView(children: Sequence<View>): R? = getNavigationView(children)
    private inline fun <reified R> BindingHelper.asBinding(binding: ViewDataBinding): R = getBinding(binding)

    private inner class NavigationViewHelper {
        inline fun <reified R> getNavigationView(view: Sequence<View>): R? {
            return view.find { it is R } as R
        }
    }

    private inner class BindingHelper {

        inline fun <reified R : ViewDataBinding> getBinding(binding: ViewDataBinding): R = binding as R
    }

    private fun bindings(binding: ViewDataBinding, navController: NavController, isTablet: Boolean) {
        when (binding) {
            is ActivityMainBinding -> initActivityMain(bindingHelper.asBinding(binding), navController, isTablet)
            else -> {
                // TODO
            }
        }
    }

    private fun initActivityMain(binding: ActivityMainBinding, navController: NavController, isTablet: Boolean) {
        if (isTablet) {
            logger { "Tablet" }
            binding.navView?.setupWithNavController(navController)
            binding.toolbar?.setNavigationOnClickListener {
                binding.drawerLayout?.openDrawer(GravityCompat.START)
            }
        } else {
            logger { "Mobile" }
            binding.bottomNavView?.setupWithBottomNavigationController(navController)
        }
    }
}

fun BottomNavigationView.setupWithBottomNavigationController(navController: NavController) {
    this.setupWithNavController(navController)
    this.setOnItemReselectedListener { }
}

fun String.show(view: View) {
    Snackbar.make(view, this, 5_000).show()
}

private fun String.logger() {
    Log.e("Log.e", this)
}

internal fun logger(msg: () -> Any) {
    msg().toString().logger()
}