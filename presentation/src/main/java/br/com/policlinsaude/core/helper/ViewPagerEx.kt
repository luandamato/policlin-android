package br.com.policlinsaude.core.helper

import android.support.v4.view.ViewPager

fun ViewPager.setupAutoscroll() {
    setupNext()
}

private fun ViewPager.setupNext() {
    postDelayed({
        val nextItem = if (currentItem + 1 == adapter?.count) 0 else currentItem + 1
        setCurrentItem(nextItem, true)
        setupNext()
    }, 5000)
}