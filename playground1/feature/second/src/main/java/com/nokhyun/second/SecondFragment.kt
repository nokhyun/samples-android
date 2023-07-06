package com.nokhyun.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nokhyun.second.databinding.FragmentSecondBinding

class SecondFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSecondBinding.inflate(layoutInflater).root
    }
}