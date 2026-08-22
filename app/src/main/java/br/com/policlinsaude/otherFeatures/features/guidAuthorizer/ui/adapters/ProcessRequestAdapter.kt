package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.AdapterPictureBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.PictureSave
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsModel

class ProcessRequestAdapter (
    private var list: MutableList<PictureSave> = arrayListOf(),
    var setOnClickListener: (PictureSave) -> Unit = { },
) : RecyclerView.Adapter<ProcessRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<PictureSave>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (PictureSave) -> Unit) {
        setOnClickListener = listener
    }

    inner class ViewHolder(private val binding: AdapterPictureBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PictureSave) {
            with(binding) {
                imageView.setImageBitmap(item.bitmap)
                imageviewDelete.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }

}