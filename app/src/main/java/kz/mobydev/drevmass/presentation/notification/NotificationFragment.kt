package kz.mobydev.drevmass.presentation.notification

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import java.util.concurrent.TimeUnit.MILLISECONDS
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.lang.System.currentTimeMillis
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentNotificationBinding
import kz.mobydev.drevmass.databinding.FragmentProfileBinding
import kz.mobydev.drevmass.model.day.DaysPostRequest
import kz.mobydev.drevmass.presentation.profile.ProfileViewModel
import kz.mobydev.drevmass.utils.NotifyWork
import kz.mobydev.drevmass.utils.NotifyWork.Companion.NOTIFICATION_ID
import kz.mobydev.drevmass.utils.NotifyWork.Companion.NOTIFICATION_WORK
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.viewModelProvider

class NotificationFragment : Fragment() {
    private val appComponents by lazy { App.appComponents }

    private var selectedDays: List<Int> =
        emptyList() // Список выбранных дней недели (0-6, где 0 - понедельник)


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private fun getViewModel(): NotificationViewModel {
        return viewModelProvider(viewModelFactory)
    }

    private val calendar: Calendar = Calendar.getInstance()
    private var selectedTime = ""

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        selectInPageSecond()
        checkNotificationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isPermission = isGranted
        }

    }

    private lateinit var checkNotificationPermission: ActivityResultLauncher<String>
    private var isPermission = false


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    appComponents.context(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                isPermission = true
            } else {
                isPermission = false

                checkNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            isPermission = true
        }
    }

//    private fun scheduleNotification(delay: Long, data: Data) {
//        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
//            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()
//
//        val instanceWorkManager = WorkManager.getInstance(appComponents.context())
//        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK,
//            ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
//    }
    private fun scheduleNotification(delay: Long, selectedDaysList: List<Int>, data: Data) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWork = PeriodicWorkRequestBuilder<NotifyWork>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, MILLISECONDS)
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val instanceWorkManager = WorkManager.getInstance(appComponents.context())
        instanceWorkManager.enqueueUniquePeriodicWork(NOTIFICATION_WORK, ExistingPeriodicWorkPolicy.REPLACE, notificationWork)

    val cancelWorkRequests = selectedDaysList.mapNotNull { day ->
        val workTag = "$NOTIFICATION_WORK-$day"
        instanceWorkManager.getWorkInfosByTag(workTag).get()
            .firstOrNull { workInfo -> workInfo.state != WorkInfo.State.CANCELLED }
            ?.id
    }

    cancelWorkRequests.forEach { workId ->
        instanceWorkManager.cancelWorkById(workId)
    }
    }




    fun selectInPageSecond() {
        getViewModel().getUserInformation(shared.getToken())
        getViewModel().user.observe(viewLifecycleOwner, Observer { data ->
            binding.let { it ->

                selectedTime = data.day?.time?.substring(0, 5) ?: "00:00"
                binding.btnChangeTime.text = data.day?.time?.substring(0, 5) ?: "00:00"
                var dayPn = data.day?.monday ?: 0
                var dayVt = data.day?.tuesday ?: 0
                var daySr = data.day?.wednesday ?: 0
                var dayCht = data.day?.thursday ?: 0
                var dayPt = data.day?.friday ?: 0
                var daySb = data.day?.saturday ?: 0
                var dayVs = data.day?.sunday ?: 0

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

                it.btnSave.setOnClickListener {
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
                    val selectedDaysList = mutableListOf<Int>()
                    if (dayPn.toString() == "1") selectedDaysList.add(0)
                    if (dayVt.toString() == "1") selectedDaysList.add(1)
                    if (daySr.toString() == "1") selectedDaysList.add(2)
                    if (dayCht.toString() == "1") selectedDaysList.add(3)
                    if (dayPt.toString() == "1") selectedDaysList.add(4)
                    if (daySb.toString() == "1") selectedDaysList.add(5)
                    if (dayVs.toString() == "1") selectedDaysList.add(6)

                    selectedDays = selectedDaysList.toList()

                    // Получение выбранного времени

                    val customCalendar = Calendar.getInstance()

                    val timeParts = selectedTime.split(":")
                    val hour = timeParts[0].toInt()
                    val minute = timeParts[1].toInt()

                    Log.d("TAG", "hour: $hour, minute: $minute")
                    Log.d("TAG", "hour: ${customCalendar.get(Calendar.HOUR)}, minute: ${customCalendar.get(Calendar.MINUTE)}")
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
                    val currentTime = currentTimeMillis()
                        val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
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
                    scheduleNotification(delay, selectedDays,data)


                    getViewModel().setNotification(shared.getToken(), dayRequest)
                    getViewModel().day.observe(viewLifecycleOwner) { observe ->
                        if (it != null)
                            findNavController().navigate(R.id.action_notificationFragment_to_profileFragment)
                    }

                }

            }
        })

    }



    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_notificationFragment_to_profileFragment)
            setOnClickExit(false)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Настройка уведомлении"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
            setOnClickBack(R.id.action_notificationFragment_to_profileFragment)
            setOnClickExit(false)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Настройка уведомлении"
            )
        }
    }


}
