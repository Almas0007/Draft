package kz.mobydev.drevmass.presentation.profile.callback

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentAboutBinding
import kz.mobydev.drevmass.databinding.FragmentCallbackBinding
import kz.mobydev.drevmass.presentation.lesssons.LessonsViewModel
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider

class CallbackFragment : Fragment() {
    private val appComponents by lazy { App.appComponents }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var message:String = ""
    private fun getViewModel(): CallbackViewModel {
        return viewModelProvider(viewModelFactory)
    }
    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentCallbackBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentCallbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextCallback.onChange {
            message = it
        }
        binding.btnSend.setOnClickListener {
            Toast(appComponents.context()).showCustomToast("Ваше обращение было отправлено", appComponents.context(),this@CallbackFragment)
            Log.d("AAA", message)
            getViewModel().sendMessageForAdmin(shared.getToken(),message.toString())
            Log.d("AAA", shared.getToken())
            getViewModel()._info.observe(viewLifecycleOwner) {
                if (it != null) {
                    findNavController().popBackStack()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_callbackFragment_to_profileFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Служба поддержки"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
            setOnClickBack(R.id.action_callbackFragment_to_profileFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Служба поддержки"
            )
        }
    }
}
