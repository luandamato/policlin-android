package br.com.policlinsaude.medicalGuideDetails.navigator

import android.content.Intent
import android.net.Uri
import br.com.policlinsaude.login.view.LoginActivity
import br.com.policlinsaude.medicalGuideDetails.view.MedicalGuideDetailsActivity
import br.com.policlinsaude.model.PresentationEstablishment

class MedicalGuideDetailsNavigatorImpl(private val activity: MedicalGuideDetailsActivity)
    : MedicalGuideDetailsNavigator {

    override fun goToCallIntent(phone: String) {
        val temp: String = "0$phone"
        activity.startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:$temp")))
    }

    override fun goToWhatsApp(phone: String) {
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=55${phone.replace(" ", "").replace("(", "").replace(")", "").replace("-","")}")
                ))
        } catch (e: Exception) {}
    }

    override fun goToShareIntent(establishment: PresentationEstablishment) {
        val intent = Intent(Intent.ACTION_SEND)
        var textToShare = "${establishment.title}\n\n" +
                "${establishment.phoneOne}\n"
        if (!establishment.phoneTwo.isEmpty()) {
            textToShare += "${establishment.phoneOne}\n"
        }
        textToShare += "\n" + establishment.getFullAddress()
        if (establishment.latitude.isNotEmpty() && establishment.longitude.isNotEmpty()) {
            textToShare += "\n" + "https://www.google.com/maps?q=${establishment.latitude.replace(",", ".")},${establishment.longitude.replace(",", ".")}"
        }
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)
        intent.type = "text/plain"
        activity.startActivity(intent)
    }

    override fun goToMapIntent(lat: String, lng: String, name: String) {
        val value = "geo:0,0?q=${lat.replace(",", ".")},${lng.replace(",", ".")}($name)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
        activity.startActivity(intent)
    }

    override fun goToLogin() {
        LoginActivity.start(activity)
    }
}