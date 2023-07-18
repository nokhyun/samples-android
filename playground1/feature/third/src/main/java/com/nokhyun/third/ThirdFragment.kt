package com.nokhyun.third

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nokhyun.third.databinding.FragmentThirdBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : Fragment() {

//    private val thirdViewModel: ThirdViewModel  by viewModels()

    private var _binding: FragmentThirdBinding? = null
    private val binding: FragmentThirdBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentThirdBinding.inflate(layoutInflater)
        return binding.run {
            lifecycleOwner = this@ThirdFragment

            root
        }
    }
}

suspend fun logger(log: suspend () -> String) {
    Log.e("logger", log())
}