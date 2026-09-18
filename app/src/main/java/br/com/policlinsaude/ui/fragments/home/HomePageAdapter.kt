package br.com.policlinsaude.ui.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.ListItemPageHomeBinding
import br.com.policlinsaude.domain.models.Banner
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.extensions.resizeAndCompress
import com.bumptech.glide.Glide

/**
 * Adapter de banners para ViewPager2 de la home.
 * Migrado/adaptado de `_legacy/.../home/view/adapter/HomePageAdapter.kt`
 * (usaba `androidx.viewpager.widget.PagerAdapter` de ViewPager 1).
 */
class HomePageAdapter(
    private val context: Context,
    private val onBannerClick: (Banner) -> Unit = {}
) : RecyclerView.Adapter<HomePageAdapter.BannerViewHolder>() {

    private val banners: MutableList<Banner> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ListItemPageHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(banners.getOrNull(position))
    }

    override fun getItemCount(): Int = banners.size

    fun updateBanners(newBanners: List<Banner>) {
        banners.clear()
        banners.addAll(newBanners)
        notifyDataSetChanged()
    }

    inner class BannerViewHolder(private val binding: ListItemPageHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(banner: Banner?) {
            if (banner == null) return
            binding.imageView.isVisible = true
            binding.imageView.setOnClickListener {
                onBannerClick.invoke(banner)
            }
            Glide.with(binding.imageView.context)
                .load(banner.image.getBitmapFromImage().resizeAndCompress(600))
                .into(binding.imageView)
        }
    }
}