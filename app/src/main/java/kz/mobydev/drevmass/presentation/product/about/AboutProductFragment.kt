package kz.mobydev.drevmass.presentation.product.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.api.MEDIA_URL
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentAboutProductBinding
import kz.mobydev.drevmass.databinding.FragmentProductBinding
import kz.mobydev.drevmass.presentation.product.ProductViewModel
import kz.mobydev.drevmass.utils.RUB_CHAR
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.viewModelProvider

class AboutProductFragment : Fragment() {
    private val args: AboutProductFragmentArgs by navArgs()

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): AboutProductViewModel {
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentAboutProductBinding

    private var videoLink:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentAboutProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerAboutProduct.startShimmer()
        binding.contentLayoutProduct.visibility = View.GONE
        observe()
    }

    private fun observe() {
        getViewModel().getProducts(shared.getToken(), args.id)
        getViewModel().product.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.contentLayoutProduct.visibility = View.VISIBLE
                binding.shimmerAboutProduct.stopShimmer()
                binding.shimmerAboutProduct.visibility = View.GONE
            }

            binding.run {
                Glide.with(appComponents.context())
                    .load(MEDIA_URL + it.imageSrc) // Замените URL на фактический путь к изображению
                    .into(binding.imgProduct)
                tvTitle.text = it.title
                val sentences = it.description.split(". ")
                val firstDescription = sentences[0].trim()
                val secondDescription =
                    sentences.subList(1, sentences.size).joinToString(". ").trim()
                binding.tvDescription.text = firstDescription
                binding.tvFullDescription.text = secondDescription
                tvPrice.text = it.price.toString() + RUB_CHAR
                tvVolume.text = "${it.height}см x ${it.length}см"
                tvWeight.text = "${it.weight} г"
                videoLink = it.videoSrc
                provideNavigationHost()?.apply {
                    visibilityTutorialButton(it.videoSrc,true)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickProfile()
            setOnClickBack(R.id.action_aboutProductFragment_to_productFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = true,
                title = ""
            )
                visibilityTutorialButton(videoLink,false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickProfile()
            setOnClickBack(R.id.action_aboutProductFragment_to_productFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = ""
            )
            visibilityTutorialButton(videoLink,false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        provideNavigationHost()?.apply {
            visibilityTutorialButton(videoLink,false)
        }
    }
}