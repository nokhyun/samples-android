package com.nokhyun.playground1

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

abstract class TestRefresh : OnRefreshListener {

    override fun onRefresh() {
        logger { "시작했니" }
        onTest().isRefreshing = false
    }

    abstract fun onTest(): SwipeRefreshLayout
}