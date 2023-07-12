package com.nokhyun.playground1

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.navigation.fragment.NavHostFragment
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

        screenSizeController.topLevelScreenSizeInit(resources.getBoolean(R.bool.isTablet), binding, navController) {
            // TODO Processing after navigation view (NavigationView 이후 처리) 공통!
        }

        navController.addOnDestinationChangedListener { _, desination, _ ->
            logger { desination }
        }

//        binding.navView?.apply {
//            val headerViewGroup = binding.navView.inflateHeaderView(R.layout.header_main_layout) as ConstraintLayout
//            headerViewGroup.children.find { it is AppCompatTextView }?.asView<AppCompatTextView>()?.let {
//                logger { it.text.toString() }
//            }
//            logger { "headerViewGroup: $headerViewGroup" }
//        }
    }
}

@Suppress("Unchecked")
fun <R> View.asView(): R {
    return this as R
}