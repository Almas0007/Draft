package kz.mobydev.drevmass.presentation.lesssons.about

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.api.MEDIA_URL
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentAboutLessonBinding
import kz.mobydev.drevmass.databinding.FragmentAboutProductBinding
import kz.mobydev.drevmass.databinding.FragmentLessonsBinding
import kz.mobydev.drevmass.presentation.product.about.AboutProductFragmentArgs
import kz.mobydev.drevmass.presentation.product.about.AboutProductViewModel
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider

class LessonAboutFragment : Fragment() {
    private val args: LessonAboutFragmentArgs by navArgs()

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): LessonAboutViewModel {
        return viewModelProvider(viewModelFactory)
    }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentAboutLessonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentAboutLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        getViewModel().getLesson(shared.getToken(), args.id)
        getViewModel().lesson.observe(viewLifecycleOwner, Observer {
            binding.tvTittleLesson.text = it.name
            binding.tvDescription.text = it.title
            binding.tvFullDescription.text = it.description

            val minutes = it.duration / 60
            val seconds = it.duration % 60

            val formattedTime = String.format("%d:%02d", minutes, seconds)
            binding.tvMinute.text = formattedTime

            Glide.with(appComponents.context())
                .load(MEDIA_URL + it.imageSrc)
                .into(binding.imgLesson)

            val focusBackground =
                ContextCompat.getDrawable(binding.root.context, R.drawable.ic_favorite_selected)
            val unFocusBackground =
                ContextCompat.getDrawable(binding.root.context, R.drawable.button_favorite)

            if (it.favorite == 1) {
                binding.btnFavorite.background = focusBackground
            } else {
                binding.btnFavorite.background = unFocusBackground
            }

            binding.btnFavorite.setOnClickListener { view ->
                val buttonFavorite = binding.btnFavorite

                if (buttonFavorite.background.constantState == focusBackground?.constantState) {
                    buttonFavorite.background = unFocusBackground
                    getViewModel().actionFavorite(shared.getToken(), it.id, "remove")
                    Toast(appComponents.context()).showCustomToast(
                        "Урок удален" + "\u2028из избранных!",
                        appComponents.context(),
                        this@LessonAboutFragment
                    )
                } else {
                    buttonFavorite.background = focusBackground
                    getViewModel().actionFavorite(shared.getToken(), it.id, "add")
                    Toast(appComponents.context()).showCustomToast(
                        "Урок добавлен\u2028в избранное!",
                        appComponents.context(),
                        this@LessonAboutFragment
                    )
                }
            }
            binding.btnTutorial.setOnClickListener { view ->
                val action =
                    LessonAboutFragmentDirections.actionLessonAboutFragmentToVideoFragment(it.videoSrc)
                findNavController().navigate(action)
            }
            binding.btnPlay.setOnClickListener { view ->
                val action =
                    LessonAboutFragmentDirections.actionLessonAboutFragmentToVideoFragment(it.videoSrc)
                findNavController().navigate(action)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(true)
            setNavigationToolBar(false)
            setOnClickProfile()
            setOnClickBack(R.id.action_lessonAboutFragment_to_lessonsFragment)
            additionalToolBarConfig(
                true,
                titleVisible = false,
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
            setOnClickBack(R.id.action_lessonAboutFragment_to_lessonsFragment)
            additionalToolBarConfig(
                true,
                titleVisible = false,
                btnProfileVisible = true,
                title = "Обучающие уроки"
            )
        }
    }
}