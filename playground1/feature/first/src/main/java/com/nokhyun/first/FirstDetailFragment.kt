package com.nokhyun.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.nokhyun.first.databinding.FragmentFirstDetailBinding

class FirstDetailFragment : Fragment() {

    private var _binding: FragmentFirstDetailBinding? = null
    private val binding: FragmentFirstDetailBinding get() = _binding!!
    private val args: FirstDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFirstDetailBinding.inflate(layoutInflater)
        sharedElementEnterTransition = android.transition.TransitionInflater.from(requireContext()).inflateTransition(R.transition.transition_test)
        return binding.run {
            lifecycleOwner = this@FirstDetailFragment

            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.ivDetail, args.firstModel.name)
    }
}