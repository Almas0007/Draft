package kz.mobydev.drevmass.presentation.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentProductBinding
import kz.mobydev.drevmass.presentation.login.LoginViewModel
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.viewModelProvider

class ProductFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): ProductViewModel {
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding:FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            shared.setToken("without_token")
            findNavController().navigate(R.id.action_productFragment_to_splashFragment)
        }
    }
    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_productFragment_to_lessonsFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = true,
                title = "Ролики для массажёра"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_productFragment_to_lessonsFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = true,
                title = "Ролики для массажёра"
            )
        }
    }
}