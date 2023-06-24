package kz.mobydev.drevmass.presentation.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.databinding.FragmentWelcomeBinding
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.viewModelProvider


class WelcomeFragment : Fragment() {


    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): WelcomeViewModel {
        return viewModelProvider(viewModelFactory)
    }

    private lateinit var binding:FragmentWelcomeBinding
    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(binding)
        binding.btnRegIn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_regInFragment)
        }
        binding.btnBack.setOnClickListener {
            viewPager2.currentItem = viewPager2.currentItem - 1
        }
        binding.skipFragment.setOnClickListener {
            viewPager2.currentItem = WelcomePageInfoList.welcomePageSlidesInfoList.size - 1
        }
        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_logInFragment)
        }

    }

    private fun setupViewPager(binding: FragmentWelcomeBinding) {
        val adapter = WelcomeAdapter()
        adapter.submitList(WelcomePageInfoList.welcomePageSlidesInfoList)
        viewPager2 = binding.viewPagerWelcome
        viewPager2.adapter = adapter

        viewPager2.registerOnPageChangeCallback(pager2Callback)
        binding.dotsIndicator.setViewPager2(viewPager2)

    }
    private val pager2Callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == WelcomePageInfoList.welcomePageSlidesInfoList.size - 1) {
                binding.btnRegIn.visibility = View.VISIBLE
            }
            else if(position==0){
                binding.btnBack.visibility = View.INVISIBLE
            }else {
                binding.btnRegIn.visibility = View.INVISIBLE
                binding.btnBack.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)

        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)

        }
    }
}