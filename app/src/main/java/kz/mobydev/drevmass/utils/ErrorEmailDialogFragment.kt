package kz.mobydev.drevmass.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ErrorEmailDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Неверный логин или пароль")
        builder.setMessage("Вы ввели не верные данные, попробуйте войти еще раз. Возможно почта уже зарегистрирована.")

        builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        return builder.create()
    }
}