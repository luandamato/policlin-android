package br.com.policlinsaude.home.view.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewpager.widget.PagerAdapter
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.resizeAndCompress
import br.com.policlinsaude.databinding.ListItemPageHomeBinding
import br.com.policlinsaude.domain.model.Banner
import com.bumptech.glide.Glide

class PhotoPageAdapter(private val context: Context) : PagerAdapter() {

    private val banners: MutableList<Banner> = ArrayList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ListItemPageHomeBinding.inflate(LayoutInflater.from(context), container, false)

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        Glide.with(container.context)
            .load(banners[position].image.getBitmapFromImage().resizeAndCompress(metrics.widthPixels))
            .into(binding.imageView)
        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, objectFromView: Any): Boolean = view == objectFromView

    override fun getCount(): Int = banners.size

    override fun destroyItem(container: ViewGroup, position: Int, objectFromView: Any) {
        container.removeView(objectFromView as View)
    }

    fun setBanners(banners: List<Banner>) {
        this.banners.clear()
        this.banners.addAll(banners)
        notifyDataSetChanged()
    }
}
