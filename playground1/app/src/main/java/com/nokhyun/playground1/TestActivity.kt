package com.nokhyun.playground1

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.nokhyun.first.ControlFocusInsetsAnimationCallback
import com.nokhyun.first.FirstAdapter
import com.nokhyun.first.FirstModel
import com.nokhyun.first.R
import com.nokhyun.first.RootViewDeferringInsetsCallback
import com.nokhyun.first.TranslateDeferringInsetsAnimationCallback
import com.nokhyun.playground1.databinding.ActivityTestBinding

class TestActivity: AppCompatActivity() {

    private var _binding: ActivityTestBinding? = null
    val binding: ActivityTestBinding get() = _binding!!

    private val firstAdapter by lazy {
        FirstAdapter { _, _ -> }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityTestBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRootView()

        with(binding){
            rvTest.adapter = firstAdapter
        }
    }

    private fun initRootView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )

        ViewCompat.setOnApplyWindowInsetsListener(binding.root, deferringInsetsListener)
        ViewCompat.setWindowInsetsAnimationCallback(binding.root, deferringInsetsListener)

        ViewCompat.setWindowInsetsAnimationCallback(
            binding.llInput,
            TranslateDeferringInsetsAnimationCallback(
                view = binding.llInput,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
        ViewCompat.setWindowInsetsAnimationCallback(
            binding.rvTest,
            TranslateDeferringInsetsAnimationCallback(
                view = binding.rvTest,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
            binding.etInput,
            ControlFocusInsetsAnimationCallback(binding.etInput)
        )
    }
}