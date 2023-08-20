package kz.mobydev.drevmass.presentation.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import kz.mobydev.drevmass.utils.AppNotificationReceiver
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
        checkNotificationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                isPermission = true
                selectInPageSecond() // Выполняем код, если разрешение предоставлено
            } else {
                isPermission = false
                // Обработка, если разрешение не было предоставлено
            }
        }

        // Запрашиваем разрешение на уведомления
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        selectInPageSecond()
        checkNotificationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isPermission = isGranted
        }

    }

    private lateinit var checkNotificationPermission: ActivityResultLauncher<String>
    private var isPermission = false



    private fun scheduleNotification(selectedDaysList: List<Int>, timeInMillis: Long) {
        val alarmManager = appComponents.context().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(appComponents.context(), AppNotificationReceiver::class.java)
        notificationIntent.putExtra("YOUR_EXTRA_DATA_KEY", "Your data here") // Pass any data you want to send with the alarm

        // Add PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_MUTABLE based on your needs
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getBroadcast(appComponents.context(), 0, notificationIntent, flags)

        // Calculate the time for the first alarm
        val currentTime = System.currentTimeMillis()
        var triggerTime = timeInMillis
        if (triggerTime <= currentTime) {
            // If the trigger time is in the past, schedule the alarm for the next occurrence of the selected days and time
            val nextTrigger = calculateNextTriggerTime(selectedDaysList, timeInMillis)
            triggerTime = nextTrigger ?: return // No valid trigger time found, return
        }

        // Set the alarm to repeat at the selected days and time
        val intervalMillis = AlarmManager.INTERVAL_DAY
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalMillis, pendingIntent)
    }
    private fun calculateNextTriggerTime(selectedDaysList: List<Int>, timeInMillis: Long): Long? {
        val calendar = Calendar.getInstance()
        val currentTime = System.currentTimeMillis()

        // Find the next occurrence of the selected days and time
        while (calendar.timeInMillis <= currentTime + 7 * AlarmManager.INTERVAL_DAY) {
            if (selectedDaysList.contains(calendar.get(Calendar.DAY_OF_WEEK) - 1)) {
                // Set the time for the selected day
                calendar.set(Calendar.HOUR_OF_DAY,
                    (timeInMillis / (60 * 60 * 1000).toInt()).toInt()
                )
                calendar.set(Calendar.MINUTE, (timeInMillis % (60 * 60 * 1000) / (60 * 1000)).toInt())
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                if (calendar.timeInMillis > currentTime) {
                    return calendar.timeInMillis
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        }
        return null // No valid trigger time found within the next 7 days
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


                    val customTime = customCalendar.timeInMillis
                    val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()

                    val selectedDaysList = mutableListOf<Int>()
                    if (dayPn.toString() == "1") selectedDaysList.add(Calendar.MONDAY)
                    if (dayVt.toString() == "1") selectedDaysList.add(Calendar.TUESDAY)
                    if (daySr.toString() == "1") selectedDaysList.add(Calendar.WEDNESDAY)
                    if (dayCht.toString() == "1") selectedDaysList.add(Calendar.THURSDAY)
                    if (dayPt.toString() == "1") selectedDaysList.add(Calendar.FRIDAY)
                    if (daySb.toString() == "1") selectedDaysList.add(Calendar.SATURDAY)
                    if (dayVs.toString() == "1") selectedDaysList.add(Calendar.SUNDAY)

                    selectedDays = selectedDaysList.toList()

                    val currentTime = System.currentTimeMillis()
                    val delay = customTime - currentTime

                    scheduleNotification(selectedDays, customTime)

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
