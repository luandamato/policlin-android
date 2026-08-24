package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.AdapterPictureBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerReponsePicturesItemsModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.QuestionsAttachmentsModel

class QuestionPicturesAdapter (
    private var list: MutableList<QuestionsAttachmentsModel> = arrayListOf(),
    var setOnClickListener: (QuestionsAttachmentsModel?) -> Unit? = { }
) : RecyclerView.Adapter<QuestionPicturesAdapter.ViewHolder>() {

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
    fun update(data: MutableList<QuestionsAttachmentsModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (QuestionsAttachmentsModel?) -> Unit) {
        setOnClickListener = listener
    }

    inner class ViewHolder(private val binding: AdapterPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionsAttachmentsModel?) {
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
                    setOnClickListener.invoke(item)
                }
            }
        }
    }

    private fun generateImageFromPdf(pdfUri: Uri, context: Context): Bitmap? {
        val fileDescriptor = context.contentResolver.openFileDescriptor(pdfUri, "r") ?: return null

        return try {
            PdfRenderer(fileDescriptor).use { renderer ->
                if (renderer.pageCount == 0) return null

                val page = renderer.openPage(0)
                val bitmap = Bitmap.createBitmap(
                    page.width,
                    page.height,
                    Bitmap.Config.ARGB_8888
                )

                page.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                page.close()

                bitmap
            }
        } catch (e: Exception) {
            null
        } finally {
            fileDescriptor.close()
        }
    }
}
