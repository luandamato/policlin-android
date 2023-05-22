package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.AdapterPictureBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerReponsePicturesItemsModel

class PicturesAdapter (
    private var list: MutableList<GuideAuthorizerReponsePicturesItemsModel> = arrayListOf(),
    var setOnClickListener: (GuideAuthorizerReponsePicturesItemsModel?) -> Unit? = {},
) : RecyclerView.Adapter<PicturesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterPictureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<GuideAuthorizerReponsePicturesItemsModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GuideAuthorizerReponsePicturesItemsModel?) {
            with(binding) {
                if(item?.link?.contains(".pdf") == true) {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(root.context, R.drawable.ic_pdf)?.apply {
                            setTint(ContextCompat.getColor(root.context, R.color.Bordo))
                        }
                    )
                } else {
                    Glide.with(root.context)
                        .load(item?.link)
                        .into(imageView)
                }
                imageviewDelete.isVisible = false

                val param = linearContainer.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0, linearContainer.marginTop, linearContainer.marginEnd, linearContainer.marginBottom)
                linearContainer.layoutParams = param

                root.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }
}
