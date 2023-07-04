package kz.mobydev.drevmass.presentation.splash

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.toast

class SplashFragment : Fragment() {
    private val appComponents by lazy { App.appComponents }
    @Inject
    lateinit var shared: PreferencesDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleScope.launch {
            delay(1.seconds)
            if ("without_token" == shared.getToken() || shared.getToken() == "") {
                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
            } else if (shared.getToken() != null && shared.getToken().isNotEmpty()) {
                val targetFragment = arguments?.getString("targetFragment")
                if (targetFragment == "InfoFragment") {
                    findNavController().navigate(R.id.action_splashFragment_to_lessonsFragment)
                }
                findNavController().navigate(R.id.action_splashFragment_to_lessonsFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        lifecycleScope.launch {
//            delay(1.seconds)
//            if ("without_token" == shared.getToken() || shared.getToken() == "") {
//                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
//            } else if (shared.getToken() != null && shared.getToken().isNotEmpty()) {
//                findNavController().navigate(R.id.action_splashFragment_to_productFragment)
//            } else {
//                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
//            }
//        }
//    }
    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
        }
    }

}