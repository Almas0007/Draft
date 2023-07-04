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
import android.widget.Button
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
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.MainActivity
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentInfoBinding
import kz.mobydev.drevmass.databinding.FragmentLogInBinding
import kz.mobydev.drevmass.databinding.FragmentWelcomeBinding
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.model.day.DaysPostRequest
import kz.mobydev.drevmass.presentation.login.LoginViewModel
import kz.mobydev.drevmass.presentation.welcome.WelcomeAdapter
import kz.mobydev.drevmass.presentation.welcome.WelcomePageInfoList
import kz.mobydev.drevmass.utils.NotifyWork
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.toast
import kz.mobydev.drevmass.utils.viewModelProvider

class InfoFragment : Fragment() {

    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private fun getViewModel(): InfoViewModel {
        return viewModelProvider(viewModelFactory)
    }
    @Inject
    lateinit var shared: PreferencesDataSource


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
        selectName = shared.getName()
        selectEmail = shared.getEmail()
        selectPassword = shared.getPassword()
        selectConfirmPassword = shared.getPassword()
        setupViewPager(binding)
        if (viewPager2Info.currentItem == 0) {
            binding.btnRegistration.text = "Следующий вопрос"
        } else {
            binding.btnRegistration.text = "Начать заниматься!"
        }
        binding.btnRegistration.setOnClickListener {

            if (viewPager2Info.currentItem == 0) {
                viewPager2Info.currentItem = 1
            } else {
                firstSend()
                secondSend()
                Log.d(
                    "AAA",
                    "onViewCreatedllsdlksdlk: " + shared.getToken() + " " + shared.getPassword()
                )
                //notification()
                getViewModel().userInfoResponse.observe(viewLifecycleOwner) {
                    if (it != null) {
                        findNavController().navigate(R.id.action_infoFragment_to_lessonsFragment)
                    }
                }
                getViewModel().errorMessage.observe(viewLifecycleOwner) {
                    Log.d("AAA", "onViewCreated ERRROR: " + it)
                }

            }
        }

        binding.btnBack.setOnClickListener {
            viewPager2Info.currentItem = 0
        }
        selectInPageFirst()
        selectInPageSecond()
    }

    private var selectName = ""
    private var selectEmail = ""
    private var selectPassword = ""
    private var selectConfirmPassword = ""
    private var selectMale = true
    private var selectHeight = "1"
    private var selectWeight = "1"
    private var selectBirthday = "2000-01-01"
    private var selectActivity = 0

    private fun firstSend() {
        val gender = if (selectMale) 1 else 0
        val userInfo = UserInfoPostRequest(
            name = selectName,
            email = selectEmail,
            password = selectPassword,
            passwordConfirmation = selectConfirmPassword,
            information = UserInfoPostRequest.Information(
                gender = gender,
                height = selectHeight.toInt() ?: 0,
                weight = selectWeight.toDouble() ?: 0.0,
                birth = selectBirthday,
                activity = selectActivity
            )
        )
        getViewModel().updateUser(shared.getToken(), userInfoPostRequest = userInfo)

    }

    private fun selectInPageFirst() {
        binding.let { it ->

            binding.btnMan.setOnClickListener {
                binding.btnMan.setTextColor(Color.WHITE)
                binding.btnMan.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnWoman.setBackgroundResource(R.drawable.background_50dp_grey_50)
                selectMale = false
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
                selectMale = true
            }
            it.tvEditTextHeight.onChange {
                selectHeight = it
            }
            it.tvEditTextWeight.onChange {
                selectWeight = it
            }


            it.btnSelectCalendar.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                    .setTheme(R.style.MaterialDatePickerTheme)


                val datePicker = builder.build()


                datePicker.show(childFragmentManager, "datePicker")

                datePicker.addOnPositiveButtonClickListener {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateString = dateFormat.format(Date(it))
                    val calendar = Calendar.getInstance()
                    calendar.time = Date(it)
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    binding.btnSelectCalendar.text = "$day.$month.$year"
                    selectBirthday = dateString ?: "2000-01-01"
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
                selectActivity = 0
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
                selectActivity = 1
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
                selectActivity = 2
            }

        }
    }

    private fun scheduleNotification(delay: Long, selectedDaysList: List<Int>, data: Data) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWork = PeriodicWorkRequestBuilder<NotifyWork>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val instanceWorkManager = WorkManager.getInstance(appComponents.context())
        instanceWorkManager.enqueueUniquePeriodicWork(
            NotifyWork.NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWork
        )

        val cancelWorkRequests = selectedDaysList.mapNotNull { day ->
            val workTag = "${NotifyWork.NOTIFICATION_WORK}-$day"
            instanceWorkManager.getWorkInfosByTag(workTag).get()
                .firstOrNull { workInfo -> workInfo.state != WorkInfo.State.CANCELLED }
                ?.id
        }

        cancelWorkRequests.forEach { workId ->
            instanceWorkManager.cancelWorkById(workId)
        }
    }

    private var dayPn = 0
    private var dayVt =0
    private var daySr =  0
    private var dayCht = 0
    private var dayPt = 0
    private var daySb = 0
    private var dayVs = 0


    fun selectInPageSecond() {
            binding.let { it ->

                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                val currentTime = String.format("%02d:%02d", currentHour, currentMinute)
                binding.btnChangeTime.text = currentTime.substring(0, 5) ?: "00:00"
                selectedTime = currentTime.substring(0, 5) ?: "00:00"

                fun updateButtonState(button: Button, dayValue: Int) {
                    if (dayValue == 0) {
                        button.setBackgroundResource(R.drawable.button_shape_line_circle)
                    } else {
                        button.setBackgroundResource(R.drawable.button_shape_line_circle_woody)
                    }
                }

                updateButtonState(it.btnPn, dayPn)
                updateButtonState(it.btnVt, dayVt)
                updateButtonState(it.btnSr, daySr)
                updateButtonState(it.btnCht, dayCht)
                updateButtonState(it.btnPt, dayPt)
                updateButtonState(it.btnSb, daySb)
                updateButtonState(it.btnVs, dayVs)

                it.btnPn.setOnClickListener { view ->
                    dayPn = if (dayPn == 0) {
                        updateButtonState(it.btnPn, 1)
                        1
                    } else {
                        updateButtonState(it.btnPn, 0)
                        0
                    }
                }

                it.btnVt.setOnClickListener { view ->
                    dayVt = if (dayVt == 0) {
                        updateButtonState(it.btnVt, 1)
                        1
                    } else {
                        updateButtonState(it.btnVt, 0)
                        0
                    }
                }

                it.btnSr.setOnClickListener { view ->
                    daySr = if (daySr == 0) {
                        updateButtonState(it.btnSr, 1)
                        1
                    } else {
                        updateButtonState(it.btnSr, 0)
                        0
                    }
                }

                it.btnCht.setOnClickListener { view ->
                    dayCht = if (dayCht == 0) {
                        updateButtonState(it.btnCht, 1)
                        1
                    } else {
                        updateButtonState(it.btnCht, 0)
                        0
                    }
                }

                it.btnPt.setOnClickListener { view ->
                    dayPt = if (dayPt == 0) {
                        updateButtonState(it.btnPt, 1)
                        1
                    } else {
                        updateButtonState(it.btnPt, 0)
                        0
                    }
                }

                it.btnSb.setOnClickListener { view ->
                    daySb = if (daySb == 0) {
                        updateButtonState(it.btnSb, 1)
                        1
                    } else {
                        updateButtonState(it.btnSb, 0)
                        0
                    }
                }

                it.btnVs.setOnClickListener { view ->
                    dayVs = if (dayVs == 0) {
                        updateButtonState(it.btnVs, 1)
                        1
                    } else {
                        updateButtonState(it.btnVs, 0)
                        0
                    }
                }

                it.btnChangeTime.setOnClickListener {
                    val timePicker = TimePickerDialog(
                        requireContext(),
                        R.style.CustomTimePickerDialogTheme,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            // Convert selected time to desired format (e.g., HH:mm)
                            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)

                            // Do something with the selected time
                            // For example, you can display it in a TextView
                            binding.btnChangeTime.text = selectedTime
                            this.selectedTime = selectedTime
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )

                    timePicker.show()
                }

            }


    }

    private fun secondSend(){
        val dayRequest = DaysPostRequest(
            monday = dayPn.toString(),
            tuesday = dayVt.toString(),
            wednesday = daySr.toString(),
            thursday = dayCht.toString(),
            friday = dayPt.toString(),
            saturday = daySb.toString(),
            sunday = dayVs.toString(),
            time = selectedTime
        )

        // Получение выбранного времени

        val customCalendar = Calendar.getInstance()

        val timeParts = selectedTime.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        Log.d("TAG", "hour: $hour, minute: $minute")
        Log.d(
            "TAG",
            "hour: ${customCalendar.get(Calendar.HOUR)}, minute: ${
                customCalendar.get(Calendar.MINUTE)
            }"
        )
        customCalendar.set(
            customCalendar.get(Calendar.YEAR), // Используем текущий год
            customCalendar.get(Calendar.MONTH), // Используем текущий месяц
            customCalendar.get(Calendar.DAY_OF_MONTH), // Используем текущий день
            hour,
            minute,
            0
        )
        val selectedDays = mutableListOf<Int>()
        if (dayPn.toString() == "1") selectedDays.add(Calendar.MONDAY)
        if (dayVt.toString() == "1") selectedDays.add(Calendar.TUESDAY)
        if (daySr.toString() == "1") selectedDays.add(Calendar.WEDNESDAY)
        if (dayCht.toString() == "1") selectedDays.add(Calendar.THURSDAY)
        if (dayPt.toString() == "1") selectedDays.add(Calendar.FRIDAY)
        if (daySb.toString() == "1") selectedDays.add(Calendar.SATURDAY)
        if (dayVs.toString() == "1") selectedDays.add(Calendar.SUNDAY)


        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()
        val data = Data.Builder().putInt(NotifyWork.NOTIFICATION_ID, 0).build()
        val delay = customTime - currentTime

        val DaysList = mutableListOf<Int>()
        DaysList.add(Calendar.MONDAY)
        DaysList.add(Calendar.TUESDAY)
        DaysList.add(Calendar.WEDNESDAY)
        DaysList.add(Calendar.THURSDAY)
        DaysList.add(Calendar.FRIDAY)
        DaysList.add(Calendar.SATURDAY)
        DaysList.add(Calendar.SUNDAY)

        Log.d("TAG", "selected days: $selectedDays")
        Log.d("TAG", "DaysList: $DaysList")

//                    scheduleNotification(delay, data)
        scheduleNotification(delay, selectedDays, data)


        getViewModel().setNotification(shared.getToken(), dayRequest)
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
                binding.secondInfoPage.visibility = View.GONE
                binding.btnBack.visibility = View.INVISIBLE
            } else {
                binding.btnRegistration.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_50dp_woody
                )
                binding.firstInfoPage.visibility = View.GONE
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