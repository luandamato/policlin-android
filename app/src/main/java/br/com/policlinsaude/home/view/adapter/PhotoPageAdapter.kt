package br.com.policlinsaude.home.view.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.viewpager.widget.PagerAdapter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import br.com.domain.model.Banner
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.resizeAndCompress
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.list_item_page_home.view.*


class PhotoPageAdapter(private val context: Context) : PagerAdapter() {

    private val banners: MutableList<Banner> = ArrayList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.list_item_page_home,
                container, false) as ViewGroup

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        Glide.with(container.context)
                .load(banners[position].image.getBitmapFromImage().resizeAndCompress(metrics.widthPixels))
                .into(layout.imageView)
        container.addView(layout)
        return layout
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