package br.com.policlinsaude.util.extensions

import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2

/**
 * Animações de view reutilizáveis.
 * Migrado/adaptado de `_legacy/.../otherFeatures/utils/AnimationExt.kt`.
 */
interface ViewAnimation {

    val easeOut: Interpolator
        get() = PathInterpolator(.37f, .46f, .63f, 1f)

    val duration: Long
        get() = 300

    val initialValue: Float
        get() = 0f

    val animationBaseValue: Float
        get() = 1400f

    fun animationRotateIconToUp(view: View, delay: Long = 0, duration: Long? = null) {
        Handler(Looper.getMainLooper()).postDelayed({
            ValueAnimator.ofFloat(0f, -180f).apply {
                interpolator = easeOut
                this.duration = duration ?: this@ViewAnimation.duration
                addUpdateListener { view.rotation = it.animatedValue as Float }
                start()
            }
        }, delay)
    }

    fun animationRotateIconToDown(view: View, delay: Long = 0, duration: Long? = null) {
        Handler(Looper.getMainLooper()).postDelayed({
            ValueAnimator.ofFloat(-180f, 0f).apply {
                interpolator = easeOut
                this.duration = duration ?: this@ViewAnimation.duration
                addUpdateListener { view.rotation = it.animatedValue as Float }
                start()
            }
        }, delay)
    }

    fun animationExpand(view: View, delay: Long = 0, duration: Long? = null) {
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                val mathParentMeasureSpec = if (view.parent is View)
                    View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY)
                else
                    View.MeasureSpec.makeMeasureSpec(view.measuredWidth, View.MeasureSpec.EXACTLY)

                val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                view.measure(mathParentMeasureSpec, wrapContentMeasureSpec)

                val targetHeight = view.measuredHeight
                view.layoutParams.height = 1
                view.visibility = View.VISIBLE
                val animation: Animation = object : Animation() {
                    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                        view.layoutParams.height = if (interpolatedTime == 1f)
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        else (targetHeight * interpolatedTime).toInt()
                        view.requestLayout()
                    }

                    override fun willChangeBounds(): Boolean = true
                }

                animation.duration = duration ?: this.duration
                animation.interpolator = easeOut

                view.startAnimation(animation)
            } catch (_: Exception) {
            }
        }, delay)
    }

    fun animationCollapse(view: View, delay: Long = 0, duration: Long? = null) {
        Handler(Looper.getMainLooper()).postDelayed({
            val initialHeight = view.measuredHeight
            val animation: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f) {
                        view.visibility = View.GONE
                    } else {
                        view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                        view.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean = true
            }

            animation.duration = duration ?: this.duration
            animation.interpolator = easeOut

            view.startAnimation(animation)
        }, delay)
    }

    fun animationChangeBottomPadding(view: View, inPadding: Int, toPadding: Int, delay: Long = 0, duration: Long? = null) {
        Handler(Looper.getMainLooper()).postDelayed({
            ValueAnimator.ofInt(inPadding, toPadding).apply {
                addUpdateListener {
                    view.setPadding(
                        view.paddingLeft,
                        view.paddingTop,
                        view.paddingRight,
                        it.animatedValue as Int
                    )
                }
                this.duration = duration ?: this@ViewAnimation.duration
                start()
            }
        }, delay)
    }
}

/**
 * Troca automática de página (ViewPager2). Adaptado do antigo `ViewPagerEx`
 * (que usava ViewPager 1).
 */
fun ViewPager2.setupAutoscroll(
    intervalMillis: Long = 5000L,
    smoothScroll: Boolean = true
) {
    val handler = Handler(Looper.getMainLooper())
    val nextRunnable = object : Runnable {
        override fun run() {
            val nextItem = if (currentItem + 1 == adapter?.itemCount) 0 else currentItem + 1
            setCurrentItem(nextItem, smoothScroll)
            handler.postDelayed(this, intervalMillis)
        }
    }
    handler.postDelayed(nextRunnable, intervalMillis)
}
// =====================================================================
// Utilidades de View
// =====================================================================

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

/** Esconde o teclado vinculado à view. */
fun View.hideKeyboard() {
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/** Mostra o teclado vinculado à view. */
fun View.showKeyboard() {
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    post { requestFocus() }
    imm.showSoftInput(this, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
}
