package com.nokhyun.playground1

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

interface ScreenSizeController {
    fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding, navController: NavController, onResult: () -> Unit)
}

class ScreenSizeControllerImpl : ScreenSizeController {

    private val navigationViewHelper2 = NavigationViewHelper2()

    override fun topLevelScreenSizeInit(isTablet: Boolean, binding: ViewDataBinding, navController: NavController, onResult: () -> Unit) {
        val children = (binding.root as ViewGroup).children

        when {
            children.any { it is NavigationView } -> {
                val navigationView = navigationViewHelper2.asNavigationView<NavigationView>(children)
                initTable(navigationView!!, navController)
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

    private fun initTable(navigationView: NavigationView, navController: NavController) {
        navigationView.setupWithNavController(navController)
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


    private class NavigationViewException(message: String) : Exception(message)
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