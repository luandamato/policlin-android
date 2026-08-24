package br.com.policlinsaude.healthInsurancePhoto.view.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.viewpager.widget.PagerAdapter
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import br.com.policlinsaude.domain.model.Banner
import br.com.policlinsaude.domain.model.HealthInsurancePhoto
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.resizeAndCompress
import br.com.policlinsaude.core.helper.rotate
import br.com.policlinsaude.databinding.ListItemPagePhotosBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class PhotoPageAdapter(private val context: Context) : PagerAdapter() {

    private val photos: MutableList<HealthInsurancePhoto> = ArrayList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        Log.d("CARTEIRINHA","NO PAGEDAPTER ------------" )
        val inflater = LayoutInflater.from(context)
        val binding = ListItemPagePhotosBinding.inflate(inflater, container, false)

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        Glide.with(container.context)
             //   .load(photos[position].imgFrente!!.getBitmapFromImage().resizeAndCompress(metrics.widthPixels))
                .load(photos[position].imgFrente!!.getBitmapFromImage().rotate(90f).resizeAndCompress(metrics.widthPixels))
                .into(binding.imageView)
        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, objectFromView: Any): Boolean = view == objectFromView

    override fun getCount(): Int = photos.size

    override fun destroyItem(container: ViewGroup, position: Int, objectFromView: Any) {
        container.removeView(objectFromView as View)
    }

    fun setPhotos(photos: List<HealthInsurancePhoto>) {
        this.photos.clear()
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }
}