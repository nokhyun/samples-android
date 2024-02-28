package com.nokhyun.first

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.nokhyun.common.awaitEnd
import com.nokhyun.first.databinding.FragmentFirstDetailBinding
import kotlinx.coroutines.launch

class FirstDetailFragment : Fragment() {

    private var _binding: FragmentFirstDetailBinding? = null
    private val binding: FragmentFirstDetailBinding get() = _binding!!
    private val args: FirstDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstDetailBinding.inflate(layoutInflater)
        sharedElementEnterTransition = android.transition.TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.transition_test)
        return binding.run {
            lifecycleOwner = this@FirstDetailFragment

            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.ivDetail, args.firstModel.name)

        animator()
    }

    private fun animator() {
        viewLifecycleOwner.lifecycleScope.launch {
            ObjectAnimator.ofFloat(binding.tvThisText, View.ALPHA, 0f, 1f).run {
                start()
                awaitEnd()
            }

            ObjectAnimator.ofFloat(
                binding.tvThisText,
                View.TRANSLATION_Y,
                -100f,
                binding.tvThisText.translationY
            ).run {
                start()
                awaitEnd()
            }

            ObjectAnimator.ofFloat(
                binding.tvThisText,
                View.TRANSLATION_X,
                -100f,
                binding.tvThisText.translationX
            ).run {
                start()
                awaitEnd()
            }
        }
    }
}