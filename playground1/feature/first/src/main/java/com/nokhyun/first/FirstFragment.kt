package com.nokhyun.first

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nokhyun.first.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.run {
            lifecycleOwner = this@FirstFragment.viewLifecycleOwner

            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCard?.setOnClickListener {
            startActivity(CardPaymentActivity::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startActivity(klass: Class<*>){
        startActivity(Intent(requireContext(), klass))
    }
}