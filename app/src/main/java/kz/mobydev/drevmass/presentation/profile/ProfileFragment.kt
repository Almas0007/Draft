package kz.mobydev.drevmass.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentProductBinding
import kz.mobydev.drevmass.databinding.FragmentProfileBinding
import kz.mobydev.drevmass.presentation.product.ProductViewModel
import kz.mobydev.drevmass.utils.InternetUtil
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider

class ProfileFragment : Fragment() {
    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): ProfileViewModel{
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val isInternetOn = InternetUtil.isInternetOn()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isInternetOn){
            Toast(appComponents.context()).showCustomToast("Нет подключения к интернету!", appComponents.context(),this@ProductFragment)
        }
        observe()
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userFragment)
        }
        binding.btnCC.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }
        binding.btnSupport.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_callbackFragment)
        }
        binding.btnRules.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_documentationFragment)
        }
        binding.btnNotification.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notificationFragment)
        }
    }

    private fun observe(){
        getViewModel().getUser(shared.getToken())
        getViewModel().userInfo.observe(viewLifecycleOwner) {
            binding.tvHelloName.text = "Привет, ${it.name}!"
            binding.tvEmail.text = it.email
        }
    }
    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_profileFragment_to_lessonsFragment)
            setOnClickExit(true)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Профиль"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
            setOnClickBack(R.id.action_productFragment_to_lessonsFragment)
            setOnClickExit(true)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Профиль"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        provideNavigationHost()?.apply {
            setOnClickExit(false)
        }
    }
}