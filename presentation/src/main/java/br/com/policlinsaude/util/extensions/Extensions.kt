package br.com.policlinsaude.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import br.com.policlinsaude.R

/**
 * Kotlin Extensions para uso em Activities, Fragments e Views
 */

// ============= Toast Extensions =============

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.showToast(message, duration)
}

// ============= Navigation Extensions =============

fun Activity.navigateTo(targetActivity: Class<out Activity>) {
    val intent = Intent(this, targetActivity)
    startActivity(intent)
}

fun Activity.navigateToWithExtras(
    targetActivity: Class<out Activity>,
    extras: Pair<String, Any>
) {
    val intent = Intent(this, targetActivity)
    when (extras.second) {
        is String -> intent.putExtra(extras.first, extras.second as String)
        is Int -> intent.putExtra(extras.first, extras.second as Int)
        is Boolean -> intent.putExtra(extras.first, extras.second as Boolean)
        is Long -> intent.putExtra(extras.first, extras.second as Long)
    }
    startActivity(intent)
}

fun Activity.finishWithResultOk() {
    setResult(Activity.RESULT_OK)
    finish()
}

// ============= View Extensions =============

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

// ============= Image Extensions =============

fun ImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty()) {
        this.setImageDrawable(null)
    } else {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}

fun ImageView.loadImageCircular(url: String?) {
    if (url.isNullOrEmpty()) {
        this.setImageDrawable(null)
    } else {
        Glide.with(this.context)
            .load(url)
            .circleCrop()
            .into(this)
    }
}

// ============= String Extensions =============

fun String?.isValidEmail(): Boolean {
    return !this.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isValidPhone(): Boolean {
    return !this.isNullOrEmpty() && this.length >= 10
}

fun String?.isNotBlankOrEmpty(): Boolean {
    return !this.isNullOrEmpty() && this.isNotBlank()
}

// ============= Collection Extensions =============

fun <T> List<T>?.isNotEmpty(): Boolean = !this.isNullOrEmpty()

fun <T> List<T>?.getOrNull(index: Int): T? {
    return if (this != null && index >= 0 && index < this.size) this[index] else null
}
