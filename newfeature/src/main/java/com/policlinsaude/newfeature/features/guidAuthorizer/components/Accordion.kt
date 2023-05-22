package com.policlinsaude.newfeature.features.guidAuthorizer.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.ComponentAccordionBinding
import com.policlinsaude.newfeature.utils.ViewAnimation

@SuppressLint("Recycle")
class Accordion(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr), ViewAnimation {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    private val binding = ComponentAccordionBinding.inflate(LayoutInflater.from(context), this, true)

    var title: String? = null
        set(value) {
            field = value
            binding.textviewTitle.isGone = value == null
            binding.textviewTitle.text = value
        }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if(childCount == 0) {
            super.addView(child, index, params)
            return
        }

        binding.inflaterView.apply {
            this.addView(child, params)
        }
    }

    init {
        setupViewAnimation()
    }

    private fun setupViewAnimation() {
        with(binding) {
            inflaterView.isVisible = true
            containerText.setOnClickListener {
                if(inflaterView.isVisible) {
                    inflaterView.visibility = View.GONE

                    animationRotateIconToDown(icon)
                } else {
                    inflaterView.visibility = View.VISIBLE
                    animationRotateIconToUp(icon)
                }
            }
        }
    }

}