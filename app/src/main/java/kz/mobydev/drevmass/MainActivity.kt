package kz.mobydev.drevmass

import android.content.Context
import android.content.res.Configuration
import kz.mobydev.drevmass.utils.NavigationHostProvider
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import java.util.Locale
import kz.mobydev.drevmass.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NavigationHostProvider {
    private val appComponents by lazy { App.appComponents }
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
                R.id.productFragment ->{
                    navController.navigate(R.id.productFragment)
                }
                R.id.lessonsFragment ->{
                    navController.navigate(R.id.lessonsFragment)
                }
                R.id.favoriteFragment ->{
                    navController.navigate(R.id.favoriteFragment)
                }
                else -> null
            } != null
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun language(){
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(
            config, baseContext.resources.displayMetrics
        )
    }
    fun statusNavBar(){
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
            navController.navigate(R.id.splashFragment)
        }
    }
}