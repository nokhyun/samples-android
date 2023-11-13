package com.nokhyun.playground1

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nokhyun.playground1.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val screenSizeController: ScreenSizeController by lazy { ScreenSizeControllerImpl() }
    private lateinit var navController: NavController

    private val statusBarHeight: Int
        @SuppressLint("DiscouragedApi", "InternalInsetResource")
        get() {
            var result = 0
            val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimension(resourceId).toInt()
            }
            return result
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        screenSizeController.topLevelScreenSizeInit(resources.getBoolean(R.bool.isTablet), binding, navController) {
            // TODO Processing after navigation view (NavigationView 이후 처리) 공통!
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.root.setPadding(0, statusBarHeight, 0, 0)

        logger { "before check" }
//        check(false) {
//
//        }

        val value1: Any? = null

        value1.guard {
            logger { "가드당해버렸다" }
            return
        }
        // TODO
        logger { "after check" }

//        binding.navView?.apply {
//            val headerViewGroup = binding.navView.inflateHeaderView(R.layout.header_main_layout) as ConstraintLayout
//            headerViewGroup.children.find { it is AppCompatTextView }?.asView<AppCompatTextView>()?.let {
//                logger { it.text.toString() }
//            }
//            logger { "headerViewGroup: $headerViewGroup" }
//        }
    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
}

@Suppress("Unchecked")
fun <R> View.asView(): R {
    return this as R
}

// TODO
inline fun <reified R> R?.guard(block: () -> Any): R? {
    return if (this == null) {
        block() // block() 위치 else 로 바꾸면 block 내부로 들어가지는건 let 이랑 사용에 대해서만... ... .. 별차이가 없는듯...
        null
    } else {
        this
    }
}
