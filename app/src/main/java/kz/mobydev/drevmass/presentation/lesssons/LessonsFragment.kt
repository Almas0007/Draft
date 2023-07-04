package kz.mobydev.drevmass.presentation.lesssons

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
import kz.mobydev.drevmass.databinding.FragmentLessonsBinding
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.utils.InternetUtil
import kz.mobydev.drevmass.utils.click.RecyclerViewFavoriteClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewItemClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewPlayClickCallback
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider

class LessonsFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): LessonsViewModel{
        return viewModelProvider(viewModelFactory)
    }

    private val content = arrayListOf<Lesson>()
    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentLessonsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val isInternetOn = InternetUtil.isInternetOn()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isInternetOn){
            Toast(appComponents.context()).showCustomToast("Нет подключения к интернету!", appComponents.context(),this@LessonsFragment)
        }
        binding.shimmerLessons.startShimmer()
        binding.shimmerLessons.visibility = View.VISIBLE
        observe()
        updateValueFavorite()
    }

    private fun updateValueFavorite(){
        getViewModel().getFavorite(shared.getToken())
        getViewModel().lessonsFavorite.observe(viewLifecycleOwner){
            Log.d("TAG", "updateValueFavorite: ${it.size}")
            provideNavigationHost()?.valueFavorite(it.size)
        }
    }
    private fun observe() {
        getViewModel().getLessons(shared.getToken())
        getViewModel().lessons.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.shimmerLessons.stopShimmer()
                binding.shimmerLessons.visibility = View.GONE
            }
            val adapter = LessonsAdapter()
            adapter.submitList(it)
            adapter.submitToken(shared.getToken())
            binding.rcLessons.adapter = adapter
            adapter.setOnItemClickListener(object : RecyclerViewItemClickCallback {
                override fun onRecyclerViewItemClick(id: Int) {
                    val action = LessonsFragmentDirections.actionLessonsFragmentToLessonAboutFragment(id)
                    findNavController().navigate(action)
                }
            })
            adapter.setOnFavoriteClickListener(object : RecyclerViewFavoriteClickCallback{
                override fun onRecyclerViewFavoriteClick(id: Int,favorite: Boolean) {
                    updateValueFavorite()
                    getViewModel().getFavorite(shared.getToken())
                        if (favorite){
                            getViewModel().actionFavorite(shared.getToken(),id,"add")
                            Toast(appComponents.context()).showCustomToast("Урок добавлен\u2028в избранное!", appComponents.context(),this@LessonsFragment)
                        }else{
                            getViewModel().actionFavorite(shared.getToken(),id,"remove")
                            Toast(appComponents.context()).showCustomToast("Урок удален" + "\u2028из избранных!", appComponents.context(),this@LessonsFragment)
                        }
                }

            })
                adapter.setOnPlayClickListener(object :RecyclerViewPlayClickCallback{
                    override fun onRecyclerViewPlayClick(url: String) {
                        val action = LessonsFragmentDirections.actionLessonsFragmentToVideoFragment(url)
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
                title = "Обучающие уроки"
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
                title = "Обучающие уроки"
            )
        }
    }

}