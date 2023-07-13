package com.nokhyun.playground1

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
