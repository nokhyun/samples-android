package com.nokhyun.playground1

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class TestRefresh : OnRefreshListener {

    override fun onRefresh() {
        "시작했니".log()
        onTest().isRefreshing = false
    }

    abstract fun onTest(): SwipeRefreshLayout
}