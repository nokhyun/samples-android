package com.nokhyun.playground1

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.nokhyun.playground1.databinding.ActivityMainBinding

interface ScreenSizeController {
    fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding, navController: NavController, onResult: () -> Unit)
}

class ScreenSizeControllerImpl : ScreenSizeController {

    private val navigationViewHelper2 = NavigationViewHelper2()
    private val bindingHelper = BindingHelper()

    override fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding, navController: NavController, onResult: () -> Unit) {
        val children = (binding.root as ViewGroup).children

        when {
            children.any { it is NavigationView } -> {
                val navigationView = navigationViewHelper2.asNavigationView<NavigationView>(children)
                initTable(navigationView!!, navController, binding)
            }

            children.any { it is BottomNavigationView } -> {
                val bottomNavigationView = navigationViewHelper2.asNavigationView<BottomNavigationView>(children)
                initMobile(bottomNavigationView!!, navController)
            }

            else -> throw NavigationViewException("Navigation view inconsistency")
        }

        onResult()
    }

    private inline fun <reified R> NavigationViewHelper2.asNavigationView(children: Sequence<View>): R? = getNavigationView(children)
    private inline fun <reified R> BindingHelper.asBinding(binding: ViewDataBinding): R = getBinding(binding)

    private fun initTable(navigationView: NavigationView, navController: NavController, binding: ViewDataBinding) {
        navigationView.setupWithNavController(navController)
        bindings(binding)
        logger { "Tablet" }
    }

    private fun initMobile(bottomNavigationView: BottomNavigationView, navController: NavController) {
        bottomNavigationView.setupWithNavController(navController)
        logger { "Mobile" }
    }

    private inner class NavigationViewHelper2 {
        inline fun <reified R> getNavigationView(view: Sequence<View>): R? {
            return view.find { it is R } as R
        }
    }

    private inner class BindingHelper {

        inline fun <reified R : ViewDataBinding> getBinding(binding: ViewDataBinding): R = binding as R
    }


    private class NavigationViewException(message: String) : Exception(message)

    private fun bindings(binding: ViewDataBinding) {
        when (binding) {
            is ActivityMainBinding -> initActivityMain(bindingHelper.asBinding(binding))
            else -> {
                // TODO
            }
        }
    }

    private fun initActivityMain(binding: ActivityMainBinding) {
        binding.toolbar?.setNavigationOnClickListener {
            binding.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }
}

internal fun String.show(view: View) {
    Snackbar.make(view, this, 5_000).show()
}

private fun String.logger() {
    Log.e("Log.e", this)
}

internal fun logger(msg: () -> Any) {
    msg().toString().logger()
}