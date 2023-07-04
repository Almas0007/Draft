package kz.mobydev.drevmass.presentation.regin

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentLogInBinding
import kz.mobydev.drevmass.databinding.FragmentRegInBinding
import kz.mobydev.drevmass.presentation.login.LoginViewModel
import kz.mobydev.drevmass.utils.ErrorEmailDialogFragment
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.toast
import kz.mobydev.drevmass.utils.viewModelProvider

class RegInFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): ReginViewModel {
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentRegInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentRegInBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var okName: Boolean = false
    private var okEmail: Boolean = false
    private var okPassword: Boolean = false
    private var okConfirmPassword: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new()
        var name: String = ""
        var login: String = ""
        var password: String = ""
        var password_confirmation: String = ""
        binding.apply {
            this.btnRegistration.setOnClickListener {
                Log.d("TAG", "$okName $okEmail $okPassword $okConfirmPassword")
                Log.d(
                    "TAG",
                    "Result: $selectName $selectEmail $selectPassword $selectConfirmPassword"
                )
                if (okName && okEmail && okPassword && okConfirmPassword) {
                    name = selectName
                    login = selectEmail
                    password = selectPassword
                    password_confirmation = selectConfirmPassword
                    shared.setPassword(password)
                    shared.setEmail(login)
                    shared.setName(name)
                    getViewModel().regInUser(name, login, password, password_confirmation)
                    getViewModel().login.observe(viewLifecycleOwner) { user ->
                        Log.d("TAG", "Result: $user")
                        shared.setToken(user.accessToken)
                        findNavController().navigate(R.id.infoFragment)
                    }
                    getViewModel().errorMessage.observe(viewLifecycleOwner) { error ->
                        Toast(appComponents.context()).showCustomToast(
                            "Ошибка при отправке, попробуйте снова!",
                            appComponents.context(),
                            this@RegInFragment
                        )
                        val dialogFragment = ErrorEmailDialogFragment()
                        dialogFragment.show(childFragmentManager, "ErrorEmailDialogFragment")
                    }
                }
            }

            this.btnBottomLogIn.setOnClickListener {
                findNavController().navigate(R.id.action_regInFragment_to_logInFragment)
            }
        }

    }

    private var selectName = ""
    private var selectEmail = ""
    private var selectPassword = ""
    private var selectConfirmPassword = ""
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private fun new() {
        binding.tvEditTextNameRegIn.onChange {
            selectName = it
            if (it.length > 1) {
                binding.tvHelperTextNameRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_200
                    )
                )
                okName = true
                if (okName && okEmail && okPassword && okConfirmPassword) {
                    binding.btnRegistration.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_50dp_woody
                    )
                }
            } else {
                binding.tvHelperTextNameRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                okName = false
            }
        }
        binding.tvEditTextEmailRegIn.onChange {
            selectEmail = it
            if (it.trim().matches(emailPattern.toRegex())) {
                binding.tvHelperTextEmailRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_200
                    )
                )
                okEmail = true
                if (okName && okEmail && okPassword && okConfirmPassword) {
                    binding.btnRegistration.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_50dp_woody
                    )
                }
            } else {
                binding.tvHelperTextEmailRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                okEmail = false
            }
        }
        binding.tvEditTextPasswordRegIn.onChange {
            if (it == "") {
                binding.tvHelperTextPasswordRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_200
                    )
                )
            } else if (it.length < 2) {
                Toast(appComponents.context()).showCustomToast(
                    "Пароль должен быть не менее 8 символов",
                    appComponents.context(),
                    this@RegInFragment
                )
            } else if (it.length < 7) {
                binding.tvHelperTextPasswordRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            } else {
                binding.tvHelperTextPasswordRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_200
                    )
                )
                selectPassword = it
                okPassword = true

                if (okName && okEmail && okPassword && okConfirmPassword) {
                    binding.btnRegistration.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_50dp_woody
                    )
                }
            }
        }
        binding.tvEditTextREPasswordRegIn.onChange {
            if (it == "" && selectPassword == shared.getPassword()) {
                binding.tvHelperTextREPasswordRegIn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_200
                    )
                )
            } else {
                if (it.length < 2) {
                    Toast(appComponents.context()).showCustomToast(
                        "Пароль должен быть не менее 8 символов",
                        appComponents.context(),
                        this@RegInFragment
                    )
                } else if (it.length < 7) {
                    binding.tvHelperTextREPasswordRegIn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                } else if (it != selectPassword) {
                    Toast(appComponents.context()).showCustomToast(
                        "Пароли должны совподать",
                        appComponents.context(),
                        this@RegInFragment
                    )
                    okConfirmPassword = false
                } else {
                    selectConfirmPassword = it
                    binding.tvHelperTextREPasswordRegIn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_200
                        )
                    )
                    okConfirmPassword = true
                    if (okName && okEmail && okPassword && okConfirmPassword) {
                        binding.btnRegistration.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.background_50dp_woody
                        )
                    }
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_regInFragment_to_logInFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Регистрация"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_regInFragment_to_logInFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Регистрация"
            )
        }
    }
}