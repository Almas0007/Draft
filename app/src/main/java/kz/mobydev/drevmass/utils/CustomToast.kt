package kz.mobydev.drevmass.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kz.mobydev.drevmass.R


fun Toast.showCustomToast(message: String, context: Context, fragment:Fragment){

    val layoutInflater = LayoutInflater.from(context)
    val layout = layoutInflater.inflate(
        R.layout.view_custom_toast,
        fragment.requireView().findViewById(R.id.toast_container)
    )

    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    this.apply {
        setGravity(Gravity.TOP, 0, 60)
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}