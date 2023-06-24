package kz.mobydev.drevmass.presentation.info

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.MainActivity
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentInfoBinding
import kz.mobydev.drevmass.databinding.FragmentLogInBinding
import kz.mobydev.drevmass.databinding.FragmentWelcomeBinding
import kz.mobydev.drevmass.presentation.login.LoginViewModel
import kz.mobydev.drevmass.presentation.welcome.WelcomeAdapter
import kz.mobydev.drevmass.presentation.welcome.WelcomePageInfoList
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.toast
import kz.mobydev.drevmass.utils.viewModelProvider

class InfoFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var shared: PreferencesDataSource


    private fun getViewModel(): InfoViewModel {
        return viewModelProvider(viewModelFactory)
    }

    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewPager2Info: ViewPager2
    private var birthDate: String = ""
    private lateinit var selectedTime: String
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var notificationManager: kz.mobydev.drevmass.utils.notification.NotificationManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(binding)

        binding.btnRegistration.setOnClickListener {
            if (viewPager2Info.currentItem == 0) {
                viewPager2Info.currentItem = 1
            } else {


                Log.d(
                    "AAA",
                    "onViewCreatedllsdlksdlk: " + shared.getToken() + " " + shared.getPassword()
                )
                getViewModel().updateUserInfo(
                    shared.getToken(),
                    shared.getName(),
                    shared.getEmail(),
                    shared.getPassword()
                )
                notification()
                findNavController().navigate(R.id.action_infoFragment_to_productFragment)

            }
        }

        binding.btnBack.setOnClickListener {
            viewPager2Info.currentItem = 0
        }
        selectInPageFirst()
        selectInPageSecond()
    }

    fun notification() {
        notificationManager =
            kz.mobydev.drevmass.utils.notification.NotificationManager(requireContext())

        val listDays = ArrayList<Boolean>()
        getViewModel().getDayList().observe(viewLifecycleOwner, Observer { it ->
            if (it.isNotEmpty()) {
                listDays.addAll(it)

                for (i in listDays.indices) {
                    if (listDays[i]) {
                        val dayOfWeek =
                            i + 1 // i + 1, так как в Calendar первый день недели - воскресенье
                        notificationManager.setNotification(selectedTime, dayOfWeek)
                    }
                }
            }
        })
    }


    private fun selectInPageFirst() {
        binding.let { it ->
            binding.btnMan.setOnClickListener {
                binding.btnMan.setTextColor(Color.WHITE)
                binding.btnMan.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnWoman.setBackgroundResource(R.drawable.background_50dp_grey_50)
                getViewModel().selectMale.postValue(true)
            }
            binding.btnWoman.setOnClickListener {
                binding.btnMan.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnWoman.setTextColor(Color.WHITE)
                binding.btnMan.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnWoman.setBackgroundResource(R.drawable.background_50dp_woody)
                getViewModel().selectMale.postValue(false)
            }
            it.tvEditTextHeight.onChange {
                getViewModel().selectHeight.postValue(it.toInt())
            }
            it.tvEditTextWeight.onChange {
                getViewModel().selectWeight.postValue(it.toDouble())
            }


            it.btnSelectCalendar.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                    .setTheme(R.style.MaterialDatePickerTheme)


                val datePicker = builder.build()


                datePicker.show(childFragmentManager, "datePicker")

                datePicker.addOnPositiveButtonClickListener {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateString = dateFormat.format(Date(it))
                    getViewModel().selectBirthDay.postValue(dateString)
                    val calendar = Calendar.getInstance()
                    calendar.time = Date(it)
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    binding.btnSelectCalendar.text = "$day.$month.$year"
                    birthDate = dateString ?: "2000-01-01"
                    getViewModel().selectBirthDay.postValue(birthDate)
                }
            }

            it.btnLow.setOnClickListener {
                binding.btnLow.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnMedium.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnHigh.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnLow.setTextColor(Color.WHITE)
                binding.btnMedium.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnHigh.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                getViewModel().selectActivity.postValue(0)
            }
            it.btnMedium.setOnClickListener {
                binding.btnLow.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnMedium.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnHigh.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnLow.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnMedium.setTextColor(Color.WHITE)
                binding.btnHigh.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                getViewModel().selectActivity.postValue(1)
            }
            it.btnHigh.setOnClickListener {
                binding.btnLow.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnMedium.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnHigh.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnLow.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnMedium.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnHigh.setTextColor(Color.WHITE)
                getViewModel().selectActivity.postValue(2)
            }

        }
    }

    fun selectInPageSecond() {
        binding.let { it ->
            var dayPn = false
            var dayVt = false
            var daySr = false
            var dayCht = false
            var dayPt = false
            var daySb = false
            var dayVs = false
            it.btnPn.setOnClickListener {
                if (dayPn) {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle)
                    dayPn = false
                    getViewModel().DayPn.postValue(false)
                } else {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle_woody_opacity)
                    dayPn = true
                    getViewModel().DayPn.postValue(true)
                }
            }
            it.btnVt.setOnClickListener {
                if (dayVt) {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle)
                    dayVt = false
                    getViewModel().DayVt.postValue(false)
                } else {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle_woody_opacity)
                    dayVt = true
                    getViewModel().DayVt.postValue(true)
                }
            }
            it.btnSr.setOnClickListener {
                if (daySr) {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle)
                    daySr = false
                    getViewModel().DaySr.postValue(false)
                } else {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle_woody_opacity)
                    daySr = true
                    getViewModel().DaySr.postValue(true)
                }
            }
            it.btnCht.setOnClickListener {
                if (dayCht) {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle)
                    dayCht = false
                    getViewModel().DayCht.postValue(false)
                } else {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle_woody_opacity)
                    dayCht = true
                    getViewModel().DayCht.postValue(true)
                }
            }
            it.btnPt.setOnClickListener {
                if (dayPt) {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle)
                    dayPt = false
                    getViewModel().DayPt.postValue(false)
                } else {
                    it.setBackgroundResource(R.drawable.button_shape_line_circle_woody_opacity)
                    dayPt = true
                    getViewModel().DayPt.postValue(true)
                }
            }
            it.btnSb.setOnClickListener {
                if (daySb) {
                    it.backgroundTintList =
                        ColorStateList.valueOf(requireContext().getColor(R.color.woody))
                    daySb = false
                    getViewModel().DaySb.postValue(false)
                } else {
                    it.backgroundTintList =
                        ColorStateList.valueOf(requireContext().getColor(R.color.woody_opacity))
                    daySb = true
                    getViewModel().DaySb.postValue(true)
                }
            }
            it.btnVs.setOnClickListener {
                if (dayVs) {
                    it.backgroundTintList =
                        ColorStateList.valueOf(requireContext().getColor(R.color.woody))
                    dayVs = false
                    getViewModel().DayVs.postValue(false)
                } else {
                    it.backgroundTintList =
                        ColorStateList.valueOf(requireContext().getColor(R.color.woody_opacity))
                    dayVs = true
                    getViewModel().DayVs.postValue(true)
                }
            }

            it.btnChangeTime.setOnClickListener {
                val timePicker = TimePickerDialog(
                    requireContext(),
                    R.style.CustomTimePickerDialogTheme,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        // Convert selected time to desired format (e.g., HH:mm)
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute)

                        // Do something with the selected time
                        // For example, you can display it in a TextView
                        binding.btnChangeTime.text = selectedTime
                        getViewModel().selectTime.postValue(selectedTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )

                timePicker.show()
            }
        }
    }


    private fun setupViewPager(binding: FragmentInfoBinding) {
        val adapter = InfoAdapter()
        adapter.submitList(InfoPageList.infoList)
        viewPager2Info = binding.viewPagerInfo
        viewPager2Info.adapter = adapter

        viewPager2Info.registerOnPageChangeCallback(pager2Callback)
        binding.dotsIndicator.setViewPager2(viewPager2Info)

    }

    private val pager2Callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == 0) {
                binding.btnRegistration.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_50dp_woody_opacity
                )
                binding.firstInfoPage.visibility = View.VISIBLE
                binding.secondInfoPage.visibility = View.INVISIBLE
                binding.btnBack.visibility = View.INVISIBLE
            } else {
                binding.btnRegistration.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_50dp_woody
                )
                binding.firstInfoPage.visibility = View.INVISIBLE
                binding.secondInfoPage.visibility = View.VISIBLE
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