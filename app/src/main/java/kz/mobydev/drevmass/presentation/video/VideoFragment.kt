package kz.mobydev.drevmass.presentation.video

import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentAboutProductBinding
import kz.mobydev.drevmass.databinding.FragmentProductBinding
import kz.mobydev.drevmass.databinding.FragmentVideoBinding
import kz.mobydev.drevmass.presentation.product.about.AboutProductFragmentArgs
import kz.mobydev.drevmass.presentation.product.about.AboutProductViewModel
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.viewModelProvider

class VideoFragment : Fragment() {
    private val args: VideoFragmentArgs by navArgs()

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentVideoBinding

    private var currentTime:Float? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        playerYouTube(args.url)

    }
    fun playerYouTube(movieLink:String){
        val youTubePlayerView = binding?.youtubePlayerView

        lifecycle.addObserver(youTubePlayerView!!)
        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                currentTime = second
            }
            override fun onReady(youTubePlayer: YouTubePlayer) {


                // using pre-made custom ui
                val defaultPlayerUiController = DefaultPlayerUiController(
                    youTubePlayerView!!, youTubePlayer
                )

                val drawableResIdLeft = R.drawable.ic_left15
                val drawableLeft: Drawable? = ContextCompat.getDrawable(appComponents.context(), drawableResIdLeft)
                if (drawableLeft != null) {
                    defaultPlayerUiController.setCustomAction1(drawableLeft, object : View.OnClickListener{
                        override fun onClick(p0: View?) {
                            youTubePlayer.seekTo(currentTime!! - 10)
                        }
                    })
                }
                val drawableResIdRight = R.drawable.ic_right15
                val drawableRight: Drawable? = ContextCompat.getDrawable(appComponents.context(), drawableResIdRight)
                if (drawableRight != null) {
                    defaultPlayerUiController.setCustomAction2(drawableRight, object : View.OnClickListener{
                        override fun onClick(p0: View?) {
                            youTubePlayer.seekTo(currentTime!! + 10)
                        }
                    })
                }

                defaultPlayerUiController.rootView.findViewById<View>(com.pierfrancescosoffritti.androidyoutubeplayer.R.id.drop_shadow_top).apply {
                    setBackgroundResource(R.drawable.ic_cancel)
                    setPadding(24)
                    updateLayoutParams {
                        width = 200
                        height = 200
                    }
                    setOnClickListener {
                        findNavController().popBackStack()
                    }
                }

                youTubePlayerView!!.setCustomPlayerUi(defaultPlayerUiController.rootView)
                defaultPlayerUiController.showYouTubeButton(false)
                defaultPlayerUiController.showFullscreenButton(false)
                defaultPlayerUiController.rootView.findViewById<com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar>(com.pierfrancescosoffritti.androidyoutubeplayer.R.id.youtube_player_seekbar).setColor(
                    resources.getColor(R.color.woody,null))

                youTubePlayer.loadOrCueVideo(lifecycle, movieLink, 0f)
            }
        }

        // disable web ui
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()

        youTubePlayerView!!.initialize(listener, options)

    }
    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }
}