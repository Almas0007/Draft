package kz.mobydev.drevmass.presentation.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentProductBinding
import kz.mobydev.drevmass.utils.InternetUtil
import kz.mobydev.drevmass.utils.click.RecyclerViewItemClickCallback
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
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

    private lateinit var binding: FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val isInternetOn = InternetUtil.isInternetOn()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isInternetOn){
            Toast(appComponents.context()).showCustomToast("Нет подключения к интернету!", appComponents.context(),this@ProductFragment)
        }
        binding.shimmerProduct.startShimmer()
        binding.shimmerProduct.visibility = View.VISIBLE
        observe()
    }

    private fun observe() {
        getViewModel().getProducts(shared.getToken())
        getViewModel().products.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.shimmerProduct.stopShimmer()
                binding.shimmerProduct.visibility = View.GONE
            }
            Log.d("TAG", "observe: $it")
            val adapter = ProductAdapter(getViewModel())
            adapter.submitList(it)
            binding.rcProduct.adapter = adapter
            adapter.setOnItemClickListener(object : RecyclerViewItemClickCallback {
                override fun onRecyclerViewItemClick(id: Int) {
                    val action =
                        ProductFragmentDirections.actionProductFragmentToAboutProductFragment(id = id)
                    findNavController().navigate(action)
                }

            })

        })
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickProfile()
            setOnClickBack(R.id.action_productFragment_to_lessonsFragment)
            additionalToolBarConfig(
                false,
                titleVisible = true,
                btnProfileVisible = true,
                title = "Массажеры и аксессуары"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickProfile()
            setOnClickBack(R.id.action_productFragment_to_lessonsFragment)
            additionalToolBarConfig(
                false,
                titleVisible = true,
                btnProfileVisible = true,
                title = "Массажеры и аксессуары"
            )
        }
    }
}