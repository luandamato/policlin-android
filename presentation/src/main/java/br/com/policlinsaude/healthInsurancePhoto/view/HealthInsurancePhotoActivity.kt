package br.com.policlinsaude.healthInsurancePhoto.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.domain.exception.MessageErrorException
import br.com.domain.model.HealthInsurancePhoto
import br.com.domain.model.HealthInsurancePhotoList
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.rotate
import br.com.policlinsaude.healthInsurancePhoto.presenter.HealthInsurancePhotoPresenter
import br.com.policlinsaude.healthInsurancePhoto.view.adapter.PhotoPageAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_health_insurance_photo.*
import java.lang.reflect.Type
import javax.inject.Inject

class HealthInsurancePhotoActivity : BaseActivity(), HealthInsurancePhotoView {


    companion object {

        fun start(activity: Activity) {
            val intent = Intent(activity, HealthInsurancePhotoActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: HealthInsurancePhotoPresenter

    @Inject
    lateinit var photoPageAdapter: PhotoPageAdapter

    private var EMPTY = ""
    private var ERROR_INPUT_EMPTY = "Please fill all fields"
    private var SAVED = "Saved!"
    private var myPreferences = "myPrefs"
    private var PHOTOS = "photosPref"
    private var PHOTO_VERSO = "photoVersoPref"
    private var PHONE_NUMBER = "photos"
    private lateinit var sharedPreferences: SharedPreferences// = getSharedPreferences(myPreferences, Context.MODE_PRIVATE) var photoVerso: String?
    private var photoVerso: String? = null

    private var isImageVerso: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_insurance_photo)
        AndroidInjection.inject(this)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        val photosPref = sharedPreferences.getString(PHOTOS, EMPTY)
        val photoVersoPref = sharedPreferences.getString(PHOTO_VERSO, EMPTY)

        Log.d("CARTEIRINHA","VALOR DE photosPrefs: " + photosPref)
        Log.d("CARTEIRINHA","VALOR DE photoVErso: " + photoVersoPref)
        presenter.getImage(this)
        setupToolbar()
    }


    override fun showDialogError(it: Throwable) {
        val listener = {
            presenter.getImage(this)
        }
        val photos: List<HealthInsurancePhoto>

        val gsonPhotosRetorno = Gson()

        val strJsonRetorno: String? = sharedPreferences.getString(PHOTOS, null)
        val strPhotoVerso: String? = sharedPreferences.getString(PHOTO_VERSO, null)

        Log.d("CARTEIRINHA","DIALOGERROR -> VALOR DE strJsonRetorno: " + strJsonRetorno)
        Log.d("CARTEIRINHA","DIALOGERROR -> VALOR DE  strPhotoVerso: " + strPhotoVerso)

        photos  = gsonPhotosRetorno.fromJson(strJsonRetorno,object:TypeToken<MutableList<HealthInsurancePhoto>>(){}.type)

        photoPageAdapter.setPhotos(photos)

        pageIndicatorViewPhoto.count = photos.size

        viewPagerPhoto.adapter = photoPageAdapter

        photoVerso = strPhotoVerso


        showDialogTryAgain(listenerPositiveButton = listener,
                message = if(it is MessageErrorException) it.message!! else "")


    }

 //   override fun showLoading() { login_progressbar.visibility = View.VISIBLE
 //   }
    override fun showLoading() { bannerProgressPhoto.visibility = View.VISIBLE
    }


 //   override fun hideLoading() { login_progressbar.visibility = View.GONE
 //   }

    override fun hideLoading() { bannerProgressPhoto.visibility = View.GONE
          }

   // override fun showImage(photo: String) {
   override fun showImage(photoListFull: HealthInsurancePhotoList) {

       Log.d("CARTEIRINHA","DENTRO DE showImage : tamanho da lista: " + photoListFull.listaimgFrente!!.size)

       var photos: List<HealthInsurancePhoto> = photoListFull.listaimgFrente!!.toMutableList()

        photoVerso = photoListFull.imgVerso



       val gsonPhotos = Gson()

       val strJson: String = gsonPhotos.toJson(photos)

       val editor = sharedPreferences.edit()


       editor.putString(PHOTOS, strJson)
       editor.putString(PHOTO_VERSO, photoVerso)

       editor.apply()

       val gsonPhotosRetorno = Gson()

       val strJsonRetorno: String? = sharedPreferences.getString(PHOTOS, null)

      photos  = gsonPhotosRetorno.fromJson(strJsonRetorno,object:TypeToken<MutableList<HealthInsurancePhoto>>(){}.type)

       val strPhotoVerso: String? = sharedPreferences.getString(PHOTO_VERSO, null)

       Log.d("CARTEIRINHA","TAMANHO DE PHOTOS: " + photos.size)
       Log.d("CARTEIRINHA","CONTEUDO DE FOTO VERSO: " + strPhotoVerso)

       photoPageAdapter.setPhotos(photos)

      photoVerso = strPhotoVerso

       pageIndicatorViewPhoto.count = photos.size

       viewPagerPhoto.adapter = photoPageAdapter
       Log.d("CARTEIRINHA","PHOTO lista tamanho: " + photoListFull.listaimgFrente!!.size)

     //  imageView.setImageBitmap(photo.getBitmapFromImage()
     //          .rotate(90f))
   //   imageView.setImageBitmap(photo.listaimgFrente!![2].imgFrente!!.getBitmapFromImage()
   //             .rotate(90f))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_health_insurance_photo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.action_verso -> {

                    if (!isImageVerso){

                        banners_containerPhoto.visibility = View.GONE
                        imageViewVerso.visibility =  View.VISIBLE

                        Log.d("CARTEIRINHA","FOTO DO VERSO: " + photoVerso)
                        presenter.onShowBackImageClicked(photoVerso)

                        isImageVerso = true
                        Log.d("CARTEIRINHA","FOTO DE VERSO")

                    }
                    else {
                        banners_containerPhoto.visibility = View.VISIBLE
                        imageViewVerso.visibility =  View.GONE
                        Log.d("CARTEIRINHA","FOTO DE FRENTE")
                        isImageVerso = false
                    }


                    return true
                }
                else -> {
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showImageVerse(photo: String?) {

        imageViewVerso.setImageBitmap(photo?.getBitmapFromImage()?.rotate(90f))
        Log.d("CARTEIRINHA","DENTRO de showImageVerse ")
    }


    override fun showWithoutNetworkDialog() {



        try {
            val photos: List<HealthInsurancePhoto>

            val gsonPhotosRetorno = Gson()

            val strJsonRetorno: String? = sharedPreferences.getString(PHOTOS, null)

            photos  = gsonPhotosRetorno.fromJson(strJsonRetorno,object:TypeToken<MutableList<HealthInsurancePhoto>>(){}.type)

            val strPhotoVerso: String? = sharedPreferences.getString(PHOTO_VERSO, null)

            photoVerso = strPhotoVerso

            photoPageAdapter.setPhotos(photos)

            pageIndicatorViewPhoto.count = photos.size

            viewPagerPhoto.adapter = photoPageAdapter

            DialogHelper.showDialog(this, getString(R.string.title_advise),
                    getString(R.string.text_no_internet_short),
                    getString(R.string.text_ok),
                    null)


        } catch (e: Exception) {

            DialogHelper.showDialog(this, getString(R.string.title_no_internet_connection),
                    getString(R.string.text_no_internet_no_photo),
                    getString(R.string.text_ok),
                    null)



        }


    }

}