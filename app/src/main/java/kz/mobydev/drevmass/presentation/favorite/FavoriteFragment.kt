package kz.mobydev.drevmass.presentation.favorite

import android.os.Bundle
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
import kz.mobydev.drevmass.databinding.FragmentFavoriteBinding
import kz.mobydev.drevmass.databinding.FragmentLessonsBinding
import kz.mobydev.drevmass.presentation.lesssons.LessonsAdapter
import kz.mobydev.drevmass.presentation.lesssons.LessonsFragmentDirections
import kz.mobydev.drevmass.presentation.lesssons.LessonsViewModel
import kz.mobydev.drevmass.utils.InternetUtil
import kz.mobydev.drevmass.utils.click.RecyclerViewFavoriteClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewItemClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewPlayClickCallback
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider

class FavoriteFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): FavoriteViewModel {
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val isInternetOn = InternetUtil.isInternetOn()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isInternetOn){
            Toast(appComponents.context()).showCustomToast("Нет подключения к интернету!", appComponents.context(),this@FavoriteFragment)
        }
        binding.shimmerFavorite.startShimmer()
        binding.shimmerFavorite.visibility = View.VISIBLE
        observe()
        updateValueFavorite()
    }
    private fun updateValueFavorite(){
        getViewModel().getFavorite(shared.getToken())
        getViewModel().favoriteList.observe(viewLifecycleOwner){
            provideNavigationHost()?.valueFavorite(it.size)
        }
    }
    private fun observe(){
        getViewModel().getFavorite(shared.getToken())
        getViewModel().favoriteList.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.shimmerFavorite.stopShimmer()
                binding.shimmerFavorite.visibility = View.GONE
            }
            val adapter = LessonsAdapter()
            adapter.submitList(it)
            adapter.submitToken(shared.getToken())
            binding.rcFavorite.adapter = adapter
            adapter.setOnItemClickListener(object : RecyclerViewItemClickCallback {
                override fun onRecyclerViewItemClick(id: Int) {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToLessonAboutFragment(id)
                    findNavController().navigate(action)
                }
            })
            adapter.setOnFavoriteClickListener(object : RecyclerViewFavoriteClickCallback {
                override fun onRecyclerViewFavoriteClick(id: Int,favorite: Boolean) {
                    updateValueFavorite()
                    getViewModel().getFavorite(shared.getToken())
                    if (favorite){
                        getViewModel().actionFavorite(shared.getToken(),id,"add")
                        Toast(appComponents.context()).showCustomToast("Урок добавлен\u2028в избранное!", appComponents.context(),this@FavoriteFragment)
                    }else{
                        getViewModel().actionFavorite(shared.getToken(),id,"remove")
                        Toast(appComponents.context()).showCustomToast("Урок удален" + "\u2028из избранных!", appComponents.context(),this@FavoriteFragment)
                    }
                }

            })
            adapter.setOnPlayClickListener(object : RecyclerViewPlayClickCallback {
                override fun onRecyclerViewPlayClick(url: String) {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToVideoFragment(url)
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
            additionalToolBarConfig(
                false,
                titleVisible = true,
                btnProfileVisible = true,
                title = "Избранные уроки"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickProfile()
            additionalToolBarConfig(
                false,
                titleVisible = true,
                btnProfileVisible = true,
                title = "Избранные уроки"
            )
        }
    }
}