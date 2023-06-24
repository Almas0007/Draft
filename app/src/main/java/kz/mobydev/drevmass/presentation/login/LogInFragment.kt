package kz.mobydev.drevmass.presentation.login

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentLogInBinding
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.toast
import kz.mobydev.drevmass.utils.viewModelProvider


class LogInFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): LoginViewModel {
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSendChangeBackgrounds()
        var login: String = ""
        var password: String = ""
        binding.apply {
            this.btnLogIn.setOnClickListener {
                login = this.tvEditTextEmailLogin.text.toString()
                password = this.tvEditTextPasswordLogin.text.toString()
                initViews(login, password)
            }
        }
        binding.btnRegIn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_regInFragment)
        }

    }

    private fun buttonSendChangeBackgrounds() {
        binding.apply {
            this.tvEditTextEmailLogin.onChange { login ->
                this.tvEditTextPasswordLogin.onChange { password ->
                    if (login.isNotEmpty() && password.isNotEmpty()) {
                        binding.btnLogIn.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.background_50dp_woody
                        )
                    } else {
                        binding.btnLogIn.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.background_50dp_woody_opacity
                        )
                    }
                }
            }
        }
    }

    private fun initViews(login: String, password: String) {
        getViewModel().validationInputValue(login, password)

        getViewModel().errorMessageLogin.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.tvHelperTextEmailLogin.setTextColor(Color.RED)
                binding.tvEditTextEmailLogin.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_email_error),
                    null,
                    null,
                    null
                )
            } else {
                binding.tvHelperTextEmailLogin.setTextColor(resources.getColor(R.color.grey_200))
                binding.tvEditTextEmailLogin.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_email),
                    null,
                    null,
                    null
                )
            }
        })
        getViewModel().errorMessagePassword.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.tvHelperTextPasswordLogin.setTextColor(Color.RED)
                binding.tvEditTextPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_password_error),
                    null,
                    null,
                    null
                )
            } else {
                binding.tvHelperTextPasswordLogin.setTextColor(resources.getColor(R.color.grey_200))
                binding.tvEditTextPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_password),
                    null,
                    null,
                    null
                )
            }
        })
        getViewModel().login.observe(viewLifecycleOwner, Observer {
            shared.setToken(binding.tvEditTextPasswordLogin.toString())
            shared.setToken(it.accessToken)
            findNavController().navigate(R.id.action_logInFragment_to_productFragment)
        })
        getViewModel().errorMessage.observe(viewLifecycleOwner, Observer {
            requireContext().toast(it)
        })
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_logInFragment_to_welcomeFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Вход"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_logInFragment_to_welcomeFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Вход"
            )
        }
    }
}