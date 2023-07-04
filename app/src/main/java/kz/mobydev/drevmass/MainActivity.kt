package kz.mobydev.drevmass

import android.content.res.Configuration
import kz.mobydev.drevmass.utils.NavigationHostProvider
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import java.util.Locale
import javax.inject.Inject
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.ActivityMainBinding
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


//        val navController = findNavController(R.id.nav_host_fragment)
//        binding.navView.setupWithNavController(navController)
        //Вариант из лучших но только не реализует то что я задумал

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

        val badgeView =
            LayoutInflater.from(this).inflate(R.layout.view_bottom_notify, mbottomNavigationMenuView, false)
        val badgeTextView = badgeView.findViewById<TextView>(R.id.notifications_badge)

        if (value == 0) {
            badgeTextView.visibility = View.GONE
            itemView.removeView(badgeView)
        } else {
            badgeTextView.visibility = View.VISIBLE
            badgeTextView.text = value.toString()
            itemView.addView(badgeView)
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
        binding.toolbarBack.setOnClickListener {
            navController.navigate(id)
        }
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
        bindNavigationViews(2,value)
    }

}


