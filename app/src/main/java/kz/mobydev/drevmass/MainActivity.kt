package kz.mobydev.drevmass

import android.content.res.Configuration
import kz.mobydev.drevmass.utils.NavigationHostProvider
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.MenuItem
import java.util.Locale
import javax.inject.Inject
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.ActivityMainBinding
import kz.mobydev.drevmass.presentation.product.about.AboutProductFragmentDirections
import kz.mobydev.drevmass.presentation.profile.user.UserFragment
import kz.mobydev.drevmass.presentation.profile.user.UserViewModel
import kz.mobydev.drevmass.utils.EnumBottomNavigationLayout
import kz.mobydev.drevmass.utils.base.BaseActivity
import kz.mobydev.drevmass.utils.toast
import kz.mobydev.drevmass.utils.viewModelProvider


class MainActivity : BaseActivity(), NavigationHostProvider {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        appComponents.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusNavBar()
        language()

        binding.bottomNavigationIndicator.updateRectByIndex(R.id.lessonsFragment,true)

        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.productFragment -> {
                    navController.navigate(R.id.productFragment)
                }

                R.id.lessonsFragment -> {
                    navController.navigate(R.id.lessonsFragment)
                }

                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                }

                else -> null
            } != null
        }

    }
    private fun bindNavigationViews(position: Int = 0, value: Int = 0) {
        val mbottomNavigationMenuView =
            binding.navView.getChildAt(0) as BottomNavigationMenuView

        val itemView = mbottomNavigationMenuView.getChildAt(position) as BottomNavigationItemView

        // Check if the badgeView is already added to the itemView
        val badgeTextView = itemView.findViewById<TextView>(R.id.notifications_badge)

        if (value == 0) {
            // If badgeTextView exists and value is 0, then hide the badgeTextView and remove the badgeView from the itemView
            if (badgeTextView != null) {
                badgeTextView.visibility = View.GONE // Hide badgeTextView
                itemView.removeView(badgeTextView.parent as View) // Remove badgeView from itemView
            }
        } else {
            // If value is not 0, then show the badgeTextView and add the badgeView to the itemView
            if (badgeTextView == null) {
                // If badgeTextView doesn't exist, create and add the badgeView
                val badgeView =
                    LayoutInflater.from(this).inflate(R.layout.view_bottom_notify, mbottomNavigationMenuView, false)
                val newBadgeTextView = badgeView.findViewById<TextView>(R.id.notifications_badge)
                newBadgeTextView.text = value.toString()
                itemView.addView(badgeView)
            } else {
                // If badgeTextView already exists, update the value
                badgeTextView.text = value.toString()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun language() {
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config, baseContext.resources.displayMetrics
        )
    }

    fun statusNavBar() {
        window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            // Проверка, поддерживает ли устройство темный режим
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Установка цвета индикаторов статусного бара на черный
                decorView.systemUiVisibility = decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.grey_black)
        }
    }

    override fun setNavigationVisibility(visible: Boolean) {
        if (visible) {
            binding.navView.visibility = View.VISIBLE
            binding.bottomNavigationIndicator.visibility = View.VISIBLE
        } else {
            binding.navView.visibility = View.GONE
            binding.bottomNavigationIndicator.visibility = View.GONE
        }
    }

    override fun setNavigationToolBar(isGone: Boolean) {
        if (isGone) {
            binding.toolbar.visibility = View.GONE
        } else {
            binding.toolbar.visibility = View.VISIBLE
        }
    }


    override fun additionalToolBarConfig(
        btnBackVisible: Boolean,
        titleVisible: Boolean,
        btnProfileVisible: Boolean,
        title: String
    ) {
        if (btnBackVisible) {
            binding.toolbarBack.visibility = View.VISIBLE
        } else {
            binding.toolbarBack.visibility = View.GONE
        }
        if (titleVisible) {
            binding.toolbarTitle.text = title
        } else {
            binding.toolbarTitle.text = ""
        }
        if (btnProfileVisible) {
            binding.toolbarProfile.visibility = View.VISIBLE
        } else {
            binding.toolbarProfile.visibility = View.GONE
        }
    }

    override fun setOnClickBack(id: Int) {
        if (id==1){binding.toolbarBack.setOnClickListener {
            navController.navigate(AboutProductFragmentDirections.actionAboutProductFragmentToVideoFragment("uR58PtUYSEc"))
        }}else{
        binding.toolbarBack.setOnClickListener {
            navController.navigate(id)
        }}
    }

    override fun setOnClickProfile() {
        binding.toolbarProfile.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }
    }

    override fun setOnClickExit(visibility: Boolean) {
        if (visibility) {
            binding.toolbarExit.visibility = View.VISIBLE
            binding.toolbarProfile.visibility = View.GONE
            binding.toolbarSave.visibility = View.GONE
        } else {
            binding.toolbarExit.visibility = View.GONE
            binding.toolbarSave.visibility = View.GONE
            binding.toolbarProfile.visibility = View.VISIBLE
        }
        binding.toolbarExit.setOnClickListener {
            shared.setToken("")
            navController.navigate(R.id.splashFragment)
        }
    }
    var saveButtonClicked = false
    override fun setOnClickSave(visibility: Boolean){
        if (visibility) {
            binding.toolbarExit.visibility = View.GONE
            binding.toolbarProfile.visibility = View.GONE
            binding.toolbarSave.visibility = View.VISIBLE
        } else {
            binding.toolbarExit.visibility = View.GONE
            binding.toolbarSave.visibility = View.GONE
            binding.toolbarProfile.visibility = View.VISIBLE
        }
        binding.toolbarSave.setOnClickListener {
           navController.navigate(R.id.action_userFragment_to_profileFragment)
        }
    }

    override fun valueFavorite(value: Int) {
        Log.d("TAG", "VAAAAAAALUE: ${value}")
        bindNavigationViews(2,value)
    }

    override fun visibilityTutorialButton(videoLink: String, visibility: Boolean) {
        if (visibility) {
            binding.btnTutorial.visibility = View.VISIBLE
            binding.btnTutorial.setOnClickListener {
                navController.navigate(AboutProductFragmentDirections.actionAboutProductFragmentToVideoFragment(videoLink))
            }
        } else {
            binding.btnTutorial.visibility = View.GONE
        }

    }

}


