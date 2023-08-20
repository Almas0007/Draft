package kz.mobydev.drevmass.presentation.reset_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentRegInBinding
import kz.mobydev.drevmass.databinding.FragmentResetPasswordBinding
import kz.mobydev.drevmass.presentation.regin.ReginViewModel
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider

class ResetPasswordFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): ResetPasswordViewModel{
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource
    private var email = ""
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding =FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextEmail.onChange {
            email = it
            if(it == ""){
                binding.btnRecover.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_50dp_woody_opacity
                )
            }
            else{
                binding.btnRecover.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_50dp_woody
                )
            }
        }
        binding.btnRecover.setOnClickListener {
            if (!binding.btnRecover.isEnabled) {
                // Кнопка уже заблокирована, игнорируем повторный клик
                return@setOnClickListener
            }

            // Блокируем кнопку, чтобы предотвратить повторные нажатия
            binding.btnRecover.isEnabled = false

            // Выполняем операцию
            observe(email)
        }
    }

    private fun observe(email: String){
        getViewModel().emailCheck(email)
        getViewModel().errorMessageLogin.observe(viewLifecycleOwner) {
            if (it == false){
                Toast(appComponents.context()).showCustomToast(
                    "Ваша почта неверно указана",
                    appComponents.context(),
                    this@ResetPasswordFragment
                )
                binding.btnRecover.isEnabled = true
            }
        }
        getViewModel().message.observe(viewLifecycleOwner) {
            when (it.email) {
                "We can't find a user with that email address." -> {
                    Toast(appComponents.context()).showCustomToast(
                        "Указаннная почта не существует",
                        appComponents.context(),
                        this@ResetPasswordFragment
                    )
                    binding.btnRecover.isEnabled = true
                }
                "Please wait before retrying." -> {
                    Toast(appComponents.context()).showCustomToast(
                        "Повторите попытку позже",
                        appComponents.context(),
                        this@ResetPasswordFragment
                    )
                    binding.btnRecover.isEnabled = true}

                else -> {
                    Toast(appComponents.context()).showCustomToast(
                        "Письмо отправлено на вашу почту",
                        appComponents.context(),
                        this@ResetPasswordFragment
                    )
                    binding.btnRecover.isEnabled = true
                    findNavController().navigate(R.id.action_resetPasswordFragment_to_logInFragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_resetPasswordFragment_to_logInFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = ""
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_resetPasswordFragment_to_logInFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = ""
            )
        }
    }
}