package com.nokhyun.playground1

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nokhyun.first.FirstInitializer
import com.nokhyun.playground1.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import java.io.IOException

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
        (application as PlaygroundApplication).initializerSet
            .onEach {
            logger { "initializer: $it" }
        }.filterIsInstance<FirstInitializer>()
            .let {
                it[0]()
            }
        some()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            logger { "controller: $controller :: destination: $destination :: arguments: $arguments" }
        }

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

    private fun some() {
        val f = flowOf(1, 2, 3, 4, 5)
            .onEach {
                logger { "onEach: $it" }
                if (it == 4) throw IOException("Test")
            }
//            .retry(3){
//                logger { "retry !!" }
//                (it is IOException).also { if(it) delay(1000L) }
//            }
            .retryWhen { cause: Throwable, attempt: Long ->
                logger { "attempt: $attempt" }
                if (cause is IOException && attempt < 2) {
                    delay(1000L)
                    true
                } else {
                    emit(100)
                    false
                }
            }
            .catch {
                logger { "catch!!" }
                if (it !is IOException) throw it
            }
//            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            f.collect {
                logger { "collect: $it" }
            }
        }
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
