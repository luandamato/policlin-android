package br.com.policlinsaude.ui.activity_healthInsurancePhoto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.ListItemPagePhotosBinding
import br.com.policlinsaude.domain.models.HealthInsurancePhoto
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.extensions.resizeAndCompress
import br.com.policlinsaude.util.extensions.rotate
import com.bumptech.glide.Glide

/**
 * Adapter do ViewPager2 da carteirinha (fotos da frente).
 *
 * Migrado do legado `PhotoPageAdapter` (era ViewPager1 + Glide; agora
 * ViewPager2 + Glide, mantendo a mesma rotação/redimensionamento).
 */
class PhotoPageAdapter(
    private val context: Context
) : RecyclerView.Adapter<PhotoPageAdapter.PageViewHolder>() {

    private val photos: MutableList<HealthInsurancePhoto> = ArrayList()

    class PageViewHolder(val binding: ListItemPagePhotosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ListItemPagePhotosBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        val imgBase64 = photos.getOrNull(position)?.imgFrente
        if (!imgBase64.isNullOrEmpty()) {
            try {
                val bitmap = imgBase64.getBitmapFromImage()
                    .rotate(90f)
                    .resizeAndCompress(metrics.widthPixels)
                Glide.with(holder.binding.root.context)
                    .load(bitmap)
                    .into(holder.binding.imageView)
            } catch (e: Exception) {
                // mantém placeholder quando o decode falha
            }
        }
    }

    override fun getItemCount(): Int = photos.size

    fun setPhotos(photos: List<HealthInsurancePhoto>) {
        this.photos.clear()
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }
}