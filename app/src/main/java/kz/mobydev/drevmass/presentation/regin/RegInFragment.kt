package kz.mobydev.drevmass.presentation.regin

import android.graphics.Color
import android.os.Bundle
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
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSendChangeBackgrounds()
        Toast.makeText(requireContext(), shared.getToken(), Toast.LENGTH_SHORT).show()
        var name: String = ""
        var login: String = ""
        var password: String = ""
        var password_confirmation: String = ""
        binding.apply {
            this.btnRegistration.setOnClickListener {
                name = this.tvEditTextNameRegIn.text.toString()
                login = this.tvEditTextEmailRegIn.text.toString()
                password = this.tvEditTextPasswordRegIn.text.toString()
                password_confirmation = this.tvEditTextREPasswordRegIn.text.toString()
                initViews(name, login, password, password_confirmation)
            }
            this.btnBottomLogIn.setOnClickListener{
                findNavController().navigate(R.id.action_regInFragment_to_logInFragment)
            }
        }

    }

    fun initViews(name:String,email:String,password:String,password_confirmation:String){
        getViewModel().validationInputValue(name, email, password, password_confirmation)

        getViewModel().errorMessageName.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.tvHelperTextNameRegIn.setTextColor(Color.RED)
                binding.tvEditTextNameRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_profile_error),
                    null,
                    null,
                    null
                )
            } else {
                binding.tvHelperTextNameRegIn.setTextColor(resources.getColor(R.color.grey_200))
                binding.tvEditTextNameRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_profile),
                    null,
                    null,
                    null
                )
            }
        })
        getViewModel().errorMessageEmail.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.tvHelperTextEmailRegIn.setTextColor(Color.RED)
                binding.tvEditTextEmailRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_email_error),
                    null,
                    null,
                    null
                )
            } else {
                binding.tvHelperTextEmailRegIn.setTextColor(resources.getColor(R.color.grey_200))
                binding.tvEditTextEmailRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_email),
                    null,
                    null,
                    null
                )
            }
        })
        getViewModel().errorMessagePassword.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.tvHelperTextPasswordRegIn.setTextColor(Color.RED)
                binding.tvEditTextPasswordRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_password_error),
                    null,
                    null,
                    null
                )
            } else {
                binding.tvHelperTextPasswordRegIn.setTextColor(resources.getColor(R.color.grey_200))
                binding.tvEditTextPasswordRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_password),
                    null,
                    null,
                    null
                )
            }
        })
        getViewModel().errorMessageConfirmPassword.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.tvHelperTextPasswordRegIn.setTextColor(Color.RED)
                binding.tvEditTextPasswordRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_password_error),
                    null,
                    null,
                    null
                )
            } else {
                binding.tvHelperTextPasswordRegIn.setTextColor(resources.getColor(R.color.grey_200))
                binding.tvEditTextPasswordRegIn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_password),
                    null,
                    null,
                    null
                )
            }
        })
        getViewModel().login.observe(viewLifecycleOwner, Observer {
            shared.setPassword(password)
            shared.setToken(it.accessToken)
            shared.setEmail(email)
            shared.setName(name)
            Toast.makeText(requireContext(), shared.getToken(), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.infoFragment)
        })
        getViewModel().errorMessage.observe(viewLifecycleOwner, Observer {
            requireContext().toast(it)
        })
    }
    private fun buttonSendChangeBackgrounds() {
        binding.apply {
            this.tvEditTextNameRegIn.onChange { name ->
                this.tvEditTextEmailRegIn.onChange { login ->
                    this.tvEditTextPasswordRegIn.onChange { password ->
                        this.tvEditTextREPasswordRegIn.onChange { password_confirmation->
                            if (name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty()&&password_confirmation.isNotEmpty()) {
                                binding.btnRegistration.background = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.background_50dp_woody
                                )
                            } else {
                                binding.btnRegistration.background = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.background_50dp_woody_opacity
                                )
                            }
                        }
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